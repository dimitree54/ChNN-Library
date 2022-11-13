package we.rashchenko.chnn.network.execution

import com.google.common.collect.HashBiMap
import we.rashchenko.utility.id.SequentialIdGenerator

interface Anonymizer<T> {
    fun anonymize(item: T): Int
    fun deAnonymize(id: Int): T
}

class SequentialAnonymizer<T> : Anonymizer<T> {
    private val map = HashBiMap.create<T, Int>()
    private val idGenerator = SequentialIdGenerator()
    override fun anonymize(item: T): Int {
        return map.getOrPut(item) { idGenerator.next }
    }

    override fun deAnonymize(id: Int): T {
        return map.inverse()[id]!!
    }
}