package we.rashchenko.chnn.network

import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import we.rashchenko.chnn.environment.EnvironmentConnectorNode
import we.rashchenko.chnn.node.Node

open class Network<ActivationType, FeedbackType>{
    protected val graph = DefaultDirectedGraph<Node<ActivationType, FeedbackType>, DefaultEdge>(DefaultEdge::class.java)
    fun getAllNodes(): Collection<Node<ActivationType, FeedbackType>>{
        return graph.vertexSet()
    }
    val nodeIds = HashMap<Node<ActivationType, FeedbackType>, Int>()
    fun addNode(node: Node<ActivationType, FeedbackType>) {
        graph.addVertex(node)
        nodeIds[node] = nodeIds.size
    }
}
