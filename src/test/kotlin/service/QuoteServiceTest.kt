package service

import Manager
import Quote
import QuoteEvent
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

internal class QuoteServiceTest {

    private val manager: Manager = Manager("republicTest")

    @Test
    fun processQuoteEvent() {
        val isin = Random.nextBytes(10).toString()
        val quoteEvent = QuoteEvent(Quote(isin, Random.nextDouble()))
        manager.quoteService.processQuoteEvent(quoteEvent)

        val findByIsin = manager.quoteDao.findByIsin(isin)
        assertNotNull(findByIsin)
        assertFalse(findByIsin.isEmpty())
    }
}