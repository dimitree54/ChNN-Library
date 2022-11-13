package we.rashchenko.utility.graph

import org.jgrapht.Graphs
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge

open class DefaultDirectedGraph<NodeType> : DirectedGraph<NodeType> {
    private val graph = DefaultDirectedGraph<NodeType, DefaultEdge>(DefaultEdge::class.java)

    override fun add(node: NodeType) {
        if (!graph.addVertex(node)) throw RuntimeException("Can not add node to network")
    }

    override fun remove(node: NodeType) {
        if (!graph.removeVertex(node)) throw IllegalArgumentException("No such node in network")
    }


    override fun cut(from: NodeType, to: NodeType) {
        graph.removeEdge(graph.getEdge(from, to) ?: throw java.lang.IllegalArgumentException("No such edge in graph"))
    }

    override fun wire(from: NodeType, to: NodeType) {
        graph.addEdge(from, to)
    }

    override val allNodes: Collection<NodeType> = graph.vertexSet()
    override fun getInputsOf(node: NodeType): Collection<NodeType> = Graphs.predecessorListOf(graph, node)
    override fun getListenersOf(node: NodeType): Collection<NodeType> = Graphs.successorListOf(graph, node)
}
