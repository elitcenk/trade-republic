package dao

import Manager
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

internal class QuoteDaoTest {

    private val manager: Manager = Manager("republicTest")

    @Test
    fun saveQuote() {
        val isin = Random.nextBytes(10).toString()
        manager.instrumentDao.save(isin, Random.nextBytes(100).toString())

        manager.quoteDao.save(isin, Random.nextDouble())

        val findByIsin = manager.quoteDao.findByIsin(isin)
        assertNotNull(findByIsin)
        assertFalse(findByIsin.isEmpty())
    }

    @Test
    fun findByIsinGroupByMinute() {
        val isin = Random.nextBytes(10).toString()
        val openPrice = 0.11
        val closePrice = 1.11
        val lowPrice = 0.01
        val highPrice = 2.11

        manager.quoteDao.save(isin, openPrice)
        manager.quoteDao.save(isin, highPrice)
        manager.quoteDao.save(isin, lowPrice)
        manager.quoteDao.save(isin, closePrice)
        val findByIsinGroupByMinute = manager.quoteDao.findByIsinGroupByMinute(isin, 1L)

        assertNotNull(findByIsinGroupByMinute)
        assertFalse(findByIsinGroupByMinute.isEmpty())
        val candle = findByIsinGroupByMinute[0]
        assertEquals(candle.highPrice, highPrice)
        assertEquals(candle.lowPrice, lowPrice)
        assertEquals(candle.openPrice, openPrice)
        assertEquals(candle.closingPrice, closePrice)
    }
}