package we.rashchenko.utility.graph

interface MutableAnonymousGraph<NodeType> : NodesCollection<NodeType>, AnonymousGraph<NodeType> {
    fun add(node: NodeType): Int
    fun remove(id: Int)

    fun wire(from: Int, to: Int)
    fun cut(from: Int, to: Int)

    fun anonymize(node: NodeType): Int
}