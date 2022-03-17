package dao

import Candlestick
import ISIN
import Quote
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoDatabase
import org.bson.Document
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class QuoteDao(database: MongoDatabase) {

    private var database: MongoDatabase? = database
    private val formatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withZone(ZoneId.systemDefault())

    fun save(isin: String, price: Double) {
        val collection = database!!.getCollection("candleStick")
        val document = Document()
        document["isin"] = isin
        document["price"] = price
        document["time"] = Instant.now()
        collection.insertOne(document)
    }

    fun findByIsin(isin: String): MutableList<Quote> {
        val collectionCandle = database!!.getCollection("candleStick")
        val searchQuery = BasicDBObject()
        searchQuery["isin"] = isin

        val quotes: MutableList<Quote> = ArrayList()
        val candleDocuments = collectionCandle.find(searchQuery)
        candleDocuments.mapTo(quotes) { Quote(it["isin"] as ISIN, it["price"] as Double) }
        return quotes
    }


    fun findByIsinGroupByMinute(isin: String, minute: Long): List<Candlestick> {
        val collection = database!!.getCollection("candleStick")
        val result = collection.aggregate(
            createGroupQuery(isin, minute)
        )

        val candlesticks: MutableList<Candlestick> = ArrayList()
        result.mapTo(candlesticks) { createCandlestick(it) }
        return candlesticks
    }

    private fun createCandlestick(document: Document): Candlestick {
        val openTimestampInstant = Instant.from(formatter.parse(document["openTimestamp"].toString()))
        val closeTimestampInstant = Instant.from(formatter.parse(document["closeTimestamp"].toString()))
        return Candlestick(
            openTimestampInstant,
            closeTimestampInstant,
            document["openPrice"].toString().toDouble(),
            document["highPrice"].toString().toDouble(),
            document["lowPrice"].toString().toDouble(),
            document["closingPrice"].toString().toDouble()
        )
    }

    private fun createGroupQuery(isin: String, minute: Long) = listOf(
        Document(
            "\$match",
            Document("isin", isin)
        ),
        Document(
            "\$group",
            Document(
                "_id",
                Document(
                    "openTimestamp",
                    Document(
                        "\$dateTrunc",
                        Document("date", "\$time")
                            .append("unit", "minute")
                            .append("binSize", minute)
                    )
                )
            )
                .append(
                    "openPrice",
                    Document("\$first", "\$price")
                )
                .append(
                    "highPrice",
                    Document("\$max", "\$price")
                )
                .append(
                    "lowPrice",
                    Document("\$min", "\$price")
                )
                .append(
                    "closePrice",
                    Document("\$last", "\$price")
                )
        ),
        Document(
            "\$project",
            Document("_id", 0L)
                .append("openTimestamp", "\$_id.openTimestamp")
                .append("openPrice", "\$openPrice")
                .append("highPrice", "\$highPrice")
                .append("lowPrice", "\$lowPrice")
                .append("closingPrice", "\$closePrice")
                .append(
                    "closeTimestamp",
                    Document(
                        "\$dateAdd",
                        Document("startDate", "\$_id.openTimestamp")
                            .append("unit", "minute")
                            .append("amount", minute)
                    )
                )
        ),
        Document(
            "\$sort",
            Document("openTimestamp", 1L)
        )
    )
}