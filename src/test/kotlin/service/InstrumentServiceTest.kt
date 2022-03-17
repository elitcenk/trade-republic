package service

import Instrument
import InstrumentEvent
import Manager
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class InstrumentServiceTest {

    private val manager: Manager = Manager("republicTest")

    @Test
    fun processInstrumentEventADD() {
        val isin = Random.nextBytes(10).toString()
        var instrumentEvent = InstrumentEvent(InstrumentEvent.Type.ADD, Instrument(isin, Random.nextBytes(100).toString()))
        manager.instrumentService.processInstrumentEvent(instrumentEvent)

        var findByIsin = manager.instrumentDao.findByIsin(isin)
        kotlin.test.assertNotNull(findByIsin)
        kotlin.test.assertFalse(findByIsin.isEmpty())

        instrumentEvent = InstrumentEvent(InstrumentEvent.Type.DELETE, Instrument(isin, Random.nextBytes(100).toString()))
        manager.instrumentService.processInstrumentEvent(instrumentEvent)

        findByIsin = manager.instrumentDao.findByIsin(isin)
        kotlin.test.assertNotNull(findByIsin)
        kotlin.test.assertTrue(findByIsin.isEmpty())
    }
}