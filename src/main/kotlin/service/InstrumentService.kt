package service

import InstrumentEvent
import dao.InstrumentDao


/**
 * Service operation of Instrument
 */
class InstrumentService(dao: InstrumentDao) {

    private var dao: InstrumentDao? = dao

    /**
     * Every Instrument element store in the mongodb collection.
     * If event type is add, data saved in to db.
     * If event type is delete, data delete from db
     */
    fun processInstrumentEvent(instrumentEvent: InstrumentEvent) {
        if (instrumentEvent.type == InstrumentEvent.Type.ADD) {
            dao?.save(instrumentEvent.data.isin, instrumentEvent.data.description)
        } else if (instrumentEvent.type == InstrumentEvent.Type.DELETE) {
            dao?.delete(instrumentEvent.data.isin)
        }
    }


}