package dao

import Manager
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class InstrumentDaoTest {

    private val manager: Manager = Manager("republicTest")

    @Test
    fun save() {
        val isin = Random.nextBytes(10).toString()
        manager.instrumentDao.save(isin, Random.nextBytes(100).toString())
        val findByIsin = manager.instrumentDao.findByIsin(isin)
        assertNotNull(findByIsin)
        assertFalse(findByIsin.isEmpty())
    }

    @Test
    fun delete() {
        val isin = Random.nextBytes(10).toString()
        manager.instrumentDao.save(isin, Random.nextBytes(100).toString())
        manager.instrumentDao.delete(isin)
        val findByIsin = manager.instrumentDao.findByIsin(isin)
        assertNotNull(findByIsin)
        assertTrue(findByIsin.isEmpty())
    }

}