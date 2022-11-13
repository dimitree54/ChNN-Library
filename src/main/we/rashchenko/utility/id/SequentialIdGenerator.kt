package we.rashchenko.utility.id

class SequentialIdGenerator : IdGenerator {
    private var lastId = 0
    override val next: Int
        get() = lastId++
}