package we.rashchenko.chnn.network

import we.rashchenko.chnn.node.Node

interface ConnectionsAdvisor<ActivationType, FeedbackType> {
    fun requestConnectionsForNewNode(): Node<ActivationType, FeedbackType>?

    fun requestExtraConnection(
        requestingNode: Node<ActivationType, FeedbackType>
    ): Node<ActivationType, FeedbackType>?

    fun requestDropConnection(
        requestingNode: Node<ActivationType, FeedbackType>,
        connectedNode: Node<ActivationType, FeedbackType>
    ): Boolean
}
