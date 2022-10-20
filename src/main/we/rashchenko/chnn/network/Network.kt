package we.rashchenko.chnn.network

import com.google.common.collect.HashBiMap
import org.jgrapht.Graphs
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import we.rashchenko.chnn.node.Node

open class Network<ActivationType, FeedbackType> {
    protected val graph = DefaultDirectedGraph<Node<ActivationType, FeedbackType>, DefaultEdge>(DefaultEdge::class.java)
    protected val nodeIds: HashBiMap<Node<ActivationType, FeedbackType>, Int> =
        HashBiMap.create<Node<ActivationType, FeedbackType>, Int>()
    private var lastId = 0
    protected fun getNextId(): Int {
        return lastId++
    }

    fun addNode(node: Node<ActivationType, FeedbackType>) {
        graph.addVertex(node)
        nodeIds[node] = getNextId()
    }

    fun removeNode(node: Node<ActivationType, FeedbackType>) {
        graph.removeVertex(node)
        nodeIds.remove(node)
    }

    fun getNode(id: Int): Node<ActivationType, FeedbackType>? {
        return nodeIds.inverse()[id]
    }

    protected open fun gatherInputs(node: Node<ActivationType, FeedbackType>): Map<Int, ActivationType> {
        return Graphs.predecessorListOf(graph, node).associateBy({ nodeIds[it]!! }, { it.getActivation() })
    }

    fun gatherInputs(nodeId: Int): Map<Int, ActivationType> {
        return gatherInputs(nodeIds.inverse()[nodeId]!!)
    }

    protected open fun gatherFeedbacks(node: Node<ActivationType, FeedbackType>): List<FeedbackType> {
        return Graphs.successorListOf(graph, node).mapNotNull { it.getFeedbacks()[nodeIds[node]!!] }
    }

    fun gatherFeedbacks(nodeId: Int): List<FeedbackType> {
        return gatherFeedbacks(nodeIds.inverse()[nodeId]!!)
    }

    protected open fun touch(
        node: Node<ActivationType, FeedbackType>,
        feedbacks: List<FeedbackType>,
        inputs: Map<Int, ActivationType>
    ) {
        node.touch(inputs)
        node.reportFeedbacks(feedbacks)
    }

    open fun touch(nodeId: Int, feedbacks: List<FeedbackType>, inputs: Map<Int, ActivationType>) {
        touch(nodeIds.inverse()[nodeId]!!, feedbacks, inputs)
    }

    fun getActivation(nodeId: Int): ActivationType {
        return nodeIds.inverse()[nodeId]!!.getActivation()
    }

    fun getFeedbacks(nodeId: Int): Map<Int, FeedbackType> {
        return nodeIds.inverse()[nodeId]!!.getFeedbacks()
    }

    fun getAllIds(): Set<Int> {
        return nodeIds.values.toSet()
    }

    fun getNodesListeningNode(nodeId: Int): Set<Int> {
        return Graphs.successorListOf(graph, nodeIds.inverse()[nodeId]!!).map { nodeIds[it]!! }.toSet()
    }

    override fun toString(): String {
        val result = StringBuilder("\n")
        graph.vertexSet().forEach { node ->
            result.append(
                "${nodeIds[node]}: ${node.getActivation()}, " +
                        "receiving inputs from ${Graphs.predecessorListOf(graph, node).map { nodeIds[it]!! }}," +
                        "feedbacks: ${node.getFeedbacks()}," +
                        "extra inputs requested: ${node.isExtraInputRequested()}\n"
            )
        }
        return result.toString()
    }
}
