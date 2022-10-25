package we.rashchenko.chnn.network

import we.rashchenko.chnn.node.Node

interface ConnectionsAdvisor<ActivationType, FeedbackType, ConnectionRequestType> {
    fun requestConnectionsForNewNode(
        connectionRequest: ConnectionRequestType,
        requestingNode: Node<ActivationType, FeedbackType, ConnectionRequestType>
    ): Node<ActivationType, FeedbackType, ConnectionRequestType>?

    fun requestExtraConnection(
        connectionRequest: ConnectionRequestType,
        requestingNode: Node<ActivationType, FeedbackType, ConnectionRequestType>
    ): Node<ActivationType, FeedbackType, ConnectionRequestType>?

    fun requestDropConnection(
        requestingNode: Node<ActivationType, FeedbackType, ConnectionRequestType>,
        connectedNode: Node<ActivationType, FeedbackType, ConnectionRequestType>
    ): Boolean
}
