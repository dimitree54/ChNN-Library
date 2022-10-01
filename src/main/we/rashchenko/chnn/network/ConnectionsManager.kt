package we.rashchenko.chnn.network

import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import we.rashchenko.chnn.node.Node

interface ConnectionsManager<ActivationType, FeedbackType> {
    fun requestExtraConnection(node: Node<ActivationType, FeedbackType>)
    fun requestDropConnection(requestingNode: Node<ActivationType, FeedbackType>, connectedNode: Node<ActivationType, FeedbackType>)
}

interface ConnectionsManagerBuilder<ActivationType, FeedbackType>{
    fun build(graph: DefaultDirectedGraph<Node<ActivationType, FeedbackType>, DefaultEdge>): ConnectionsManager<ActivationType, FeedbackType>
}