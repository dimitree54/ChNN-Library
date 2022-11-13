package we.rashchenko.utility.graph

interface NodesCollection<NodeType> {
    val allNodes: Collection<NodeType>
}

interface DirectedGraph<NodeType> : NodesCollection<NodeType> {

    fun add(node: NodeType)
    fun remove(node: NodeType)

    fun wire(from: NodeType, to: NodeType)
    fun cut(from: NodeType, to: NodeType)

    fun getInputsOf(node: NodeType): Collection<NodeType>
    fun getListenersOf(node: NodeType): Collection<NodeType>
}
