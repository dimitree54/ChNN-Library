package we.rashchenko.utility.id

import com.google.common.collect.HashBiMap

class DefaultBiAnonymizer<T>(
    private val idGenerator: IdGenerator,
) : BiAnonymizer<T> {
    private val map = HashBiMap.create<T, Int>()
    override fun anonymize(item: T): Int {
        return map.getOrPut(item) { idGenerator.next }
    }

    override fun deAnonymize(id: Int): T {
        return map.inverse()[id]!!
    }
}