package service

import Candlestick
import CandlestickManager
import QuoteEvent
import dao.QuoteDao

class QuoteService(dao: QuoteDao) : CandlestickManager{

    private var dao: QuoteDao? = dao

    //quote event data is saved in a timeseries collection in mongo which is retained for only
    // 30 mins(1800 secs) based on the time field provided
    fun processQuoteEvent(quoteEvent: QuoteEvent) {
        dao?.save(quoteEvent.data.isin, quoteEvent.data.price)
    }

    override fun getCandlesticks(isin: String): List<Candlestick> {
        val findByIsinGroupByMinute = dao?.findByIsinGroupByMinute(isin, 1L)
        return findByIsinGroupByMinute!!
    }
}