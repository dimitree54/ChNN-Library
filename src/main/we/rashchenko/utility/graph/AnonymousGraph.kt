package we.rashchenko.utility.graph

interface AnonymousGraph<out NodeType> {
    val allIds: Collection<Int>
    fun deAnonymize(id: Int): NodeType
    fun getInputsOf(id: Int): Collection<Int>
    fun getListenersOf(id: Int): Collection<Int>
}