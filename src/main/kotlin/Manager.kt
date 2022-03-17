import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.CreateCollectionOptions
import com.mongodb.client.model.TimeSeriesOptions
import dao.InstrumentDao
import dao.QuoteDao
import service.InstrumentService
import service.QuoteService
import java.util.concurrent.TimeUnit

class Manager(databaseName: String = "republic") {
    private val database = mongoSetup(databaseName)
    val instrumentDao: InstrumentDao = InstrumentDao(database)
    val quoteDao: QuoteDao = QuoteDao(database)
    val instrumentService: InstrumentService = InstrumentService(instrumentDao)
    val quoteService: QuoteService = QuoteService(quoteDao)

    /**
     * Candlestick and instrument collection created in mongo. Candlestick collection is time series. These collections retain last 30 minutes.
     */
    private fun mongoSetup(databaseName: String): MongoDatabase {
        val mongoClient = MongoClient("mongodb://root:example@localhost:27017")
        val database = mongoClient.getDatabase(databaseName)
        database.drop()
        val tsOptions = TimeSeriesOptions("time")
        tsOptions.metaField("isin")
        val collOptions = CreateCollectionOptions().timeSeriesOptions(tsOptions).expireAfter(1800, TimeUnit.SECONDS)
        database.createCollection("candleStick", collOptions)
        database.createCollection("instrument")
        return database
    }
}