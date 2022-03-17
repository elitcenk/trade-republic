package service

import Candlestick
import CandlestickManager
import QuoteEvent
import dao.QuoteDao

class QuoteService(dao: QuoteDao) : CandlestickManager{

    private var dao: QuoteDao? = dao

    /**
     * Quote every data to mongodb time series collection. This collection store elements only 30(1800 seconds) minutes
     */
    fun processQuoteEvent(quoteEvent: QuoteEvent) {
        dao?.save(quoteEvent.data.isin, quoteEvent.data.price)
    }

    /**
     * Get last 30 minutes 1 minutes candlestick
     */
    override fun getCandlesticks(isin: String): List<Candlestick> {
        val findByIsinGroupByMinute = dao?.findByIsinGroupByMinute(isin, 1L)
        return findByIsinGroupByMinute!!
    }
}