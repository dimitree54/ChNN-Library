package we.rashchenko.chnn.network

import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import we.rashchenko.chnn.node.Node

interface ConnectionsAdvisor<ActivationType, FeedbackType> {
    fun requestConnectionsForNewNode(): List<Node<ActivationType, FeedbackType>>
    fun requestExtraConnection(node: Node<ActivationType, FeedbackType>): List<Node<ActivationType, FeedbackType>>
    fun requestDropConnection(requestingNode: Node<ActivationType, FeedbackType>, connectedNode: Node<ActivationType, FeedbackType>): Boolean
}

interface ConnectionsAdvisorBuilder<ActivationType, FeedbackType>{
    fun build(
        graph: DefaultDirectedGraph<Node<ActivationType, FeedbackType>, DefaultEdge>
    ): ConnectionsAdvisor<ActivationType, FeedbackType>
}