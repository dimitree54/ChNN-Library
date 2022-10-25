package we.rashchenko.chnn.network

import we.rashchenko.chnn.node.Node

class SelfMorphingNetwork<ActivationType, FeedbackType, ConnectionRequestType>(
    private val connectionsAdvisor: ConnectionsAdvisor<ActivationType, FeedbackType, ConnectionRequestType>,
) : Network<ActivationType, FeedbackType, ConnectionRequestType>() {
    private fun addInput(
        request: ConnectionRequestType,
        node: Node<ActivationType, FeedbackType, ConnectionRequestType>,
        extraInput: Node<ActivationType, FeedbackType, ConnectionRequestType>,
    ) {
        if (extraInput !in nodeIds) {
            val inputForNewNode = connectionsAdvisor.requestConnectionsForNewNode(request, extraInput) ?: return
            nodeIds[extraInput] = getNextId()
            graph.addVertex(extraInput)
            addInput(request, extraInput, inputForNewNode)
        }
        node.addInput(nodeIds[extraInput]!!)
        graph.addEdge(extraInput, node)
    }

    private fun removeInput(
        node: Node<ActivationType, FeedbackType, ConnectionRequestType>,
        removeNode: Node<ActivationType, FeedbackType, ConnectionRequestType>,
    ) {
        val removeNodeId = nodeIds[removeNode]!!
        node.removeInput(removeNodeId)
        graph.removeEdge(removeNode, node)
        if (graph.inDegreeOf(removeNode) == 0) {
            removeNode(removeNode)
            graph.removeVertex(removeNode)
        }
    }

    private fun morph(node: Node<ActivationType, FeedbackType, ConnectionRequestType>) {
        node.extraInputRequest?.let { request ->
            connectionsAdvisor.requestExtraConnection(request, node)?.also { addInput(request, node, it) }
        }
        node.inputsRemoveRequest.forEach { removeNodeId ->
            nodeIds.inverse()[removeNodeId]?.also { removeNode ->
                if (connectionsAdvisor.requestDropConnection(node, removeNode)) {
                    removeInput(node, removeNode)
                }
            }
        }
    }

    override fun touch(
        node: Node<ActivationType, FeedbackType, ConnectionRequestType>,
        feedbacks: List<FeedbackType>,
        inputs: Map<Int, ActivationType>,
    ) {
        super.touch(node, feedbacks, inputs)
        morph(node)
    }
}