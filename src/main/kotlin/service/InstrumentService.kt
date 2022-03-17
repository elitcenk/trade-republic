package service

import InstrumentEvent
import dao.InstrumentDao


class InstrumentService(dao: InstrumentDao) {

    private var dao: InstrumentDao? = dao

    fun processInstrumentEvent(instrumentEvent: InstrumentEvent) {
        if (instrumentEvent.type == InstrumentEvent.Type.ADD) {
            dao?.save(instrumentEvent.data.isin, instrumentEvent.data.description)
        } else if (instrumentEvent.type == InstrumentEvent.Type.DELETE) {
            dao?.delete(instrumentEvent.data.isin)
        }
    }


}