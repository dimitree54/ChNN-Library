package we.rashchenko.chnn.network

import com.google.common.collect.HashBiMap
import org.jgrapht.Graphs
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import we.rashchenko.chnn.node.Node

open class Network<ActivationType, FeedbackType>{
    protected val graph = DefaultDirectedGraph<Node<ActivationType, FeedbackType>, DefaultEdge>(DefaultEdge::class.java)
    protected val nodeIds: HashBiMap<Node<ActivationType, FeedbackType>, Int> = HashBiMap.create<Node<ActivationType, FeedbackType>, Int>()
    private var lastId = 0
    fun addNode(node: Node<ActivationType, FeedbackType>) {
        graph.addVertex(node)
        nodeIds[node] = lastId++
    }
    fun removeNode(node: Node<ActivationType, FeedbackType>) {
        graph.removeVertex(node)
        nodeIds.remove(node)
    }

    protected open fun gatherInputs(node: Node<ActivationType, FeedbackType>): Map<Int, ActivationType> {
        return Graphs.predecessorListOf(graph, node).associateBy({ nodeIds[it]!! }, { it.getActivation() })
    }

    protected open fun gatherFeedbacks(node: Node<ActivationType, FeedbackType>): List<FeedbackType> {
        return Graphs.successorListOf(graph, node ).mapNotNull { it.getFeedbacks()[nodeIds[node]!!] }
    }

    protected open fun touch(node: Node<ActivationType, FeedbackType>){
        node.reportFeedbacks(gatherFeedbacks(node))
        node.touch(gatherInputs(node))
    }
}
