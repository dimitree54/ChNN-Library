package we.rashchenko.utility.graph

import org.jgrapht.Graphs
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import we.rashchenko.utility.id.BiAnonymizer

class DefaultMutableAnonymousGraph<NodeType>(
    private val biAnonymizer: BiAnonymizer<NodeType>,
) : MutableAnonymousGraph<NodeType> {
    private val nodesGraph = DefaultDirectedGraph<NodeType, DefaultEdge>(DefaultEdge::class.java)
    private val idsGraph = DefaultDirectedGraph<Int, DefaultEdge>(DefaultEdge::class.java)

    override fun add(node: NodeType): Int {
        val id = biAnonymizer.anonymize(node)
        nodesGraph.addVertex(node)
        idsGraph.addVertex(id)
        return id
    }

    override fun remove(id: Int) {
        val node = biAnonymizer.deAnonymize(id)
        nodesGraph.removeVertex(node)
        idsGraph.removeVertex(id)
    }

    override fun wire(from: Int, to: Int) {
        val fromNode = biAnonymizer.deAnonymize(from)
        val toNode = biAnonymizer.deAnonymize(to)
        nodesGraph.addEdge(fromNode, toNode)
        idsGraph.addEdge(from, to)
    }

    override fun cut(from: Int, to: Int) {
        val fromNode = biAnonymizer.deAnonymize(from)
        val toNode = biAnonymizer.deAnonymize(to)
        nodesGraph.removeEdge(fromNode, toNode)
        idsGraph.removeEdge(from, to)
    }

    override val allNodes: Collection<NodeType> = nodesGraph.vertexSet()
    override val allIds: Collection<Int> = idsGraph.vertexSet()
    override fun deAnonymize(id: Int): NodeType = biAnonymizer.deAnonymize(id)
    override fun anonymize(node: NodeType): Int = biAnonymizer.anonymize(node)
    override fun getInputsOf(id: Int): Collection<Int> = Graphs.predecessorListOf(idsGraph, id)
    override fun getListenersOf(id: Int): Collection<Int> = Graphs.successorListOf(idsGraph, id)
}
