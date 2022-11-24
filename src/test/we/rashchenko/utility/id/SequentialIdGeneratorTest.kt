package we.rashchenko.utility.id

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SequentialIdGeneratorTest {

    @Test
    fun testUniqueness() {
        val numExamples = 1000000
        val generator = SequentialIdGenerator()
        val uniqueIds = (0 until numExamples).map { generator.next }
        assertEquals(numExamples, uniqueIds.size)
    }
}