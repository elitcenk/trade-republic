package dao

import ISIN
import Instrument
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoDatabase
import org.bson.Document


class InstrumentDao(database: MongoDatabase) {

    private var database: MongoDatabase? = database

    /**
     * Save Instrument in mongodb instrument collection
     */
    fun save(isin: String, desc: String) {
        val collection = database!!.getCollection("instrument")
        val document = Document()
        document["isin"] = isin
        document["desc"] = desc
        collection.insertOne(document)
    }


    /**
     * Delete instrument by ISIN from mongodb
     */
    fun delete(isin: String) {
        val collectionInstrument = database!!.getCollection("instrument")
        val collectionCandle = database!!.getCollection("candleStick")
        val searchQuery = BasicDBObject()
        searchQuery["isin"] = isin
        collectionInstrument.deleteOne(searchQuery)
        collectionCandle.deleteMany(searchQuery)
    }

    /**
     * Find Instrument list by ISIN from mongodb
     */
    fun findByIsin(isin: String): MutableList<Instrument> {
        val collectionInstrument = database!!.getCollection("instrument")
        val searchQuery = BasicDBObject()
        searchQuery["isin"] = isin

        val instruments: MutableList<Instrument> = ArrayList()
        val instrumentDocuments = collectionInstrument.find(searchQuery)
        instrumentDocuments.mapTo(instruments) { Instrument(it["isin"] as ISIN,  it["desc"] as String) }
        return instruments
    }
}