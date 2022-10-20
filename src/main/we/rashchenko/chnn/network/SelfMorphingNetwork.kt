package we.rashchenko.chnn.network

import we.rashchenko.chnn.node.Node

class SelfMorphingNetwork<ActivationType, FeedbackType>(
    private val connectionsAdvisor: ConnectionsAdvisor<ActivationType, FeedbackType>
) : Network<ActivationType, FeedbackType>() {
    private fun addInput(
        node: Node<ActivationType, FeedbackType>,
        extraInput: Node<ActivationType, FeedbackType>
    ) {
        if (extraInput !in nodeIds) {
            val inputForNewNode = connectionsAdvisor.requestConnectionsForNewNode() ?: return
            nodeIds[extraInput] = getNextId()
            graph.addVertex(extraInput)
            addInput(extraInput, inputForNewNode)
        }
        node.addInput(nodeIds[extraInput]!!)
        graph.addEdge(extraInput, node)
    }

    private fun removeInput(
        node: Node<ActivationType, FeedbackType>,
        removeNode: Node<ActivationType, FeedbackType>
    ) {
        val removeNodeId = nodeIds[removeNode]!!
        node.removeInput(removeNodeId)
        graph.removeEdge(removeNode, node)
        if (graph.inDegreeOf(removeNode) == 0) {
            removeNode(removeNode)
            graph.removeVertex(removeNode)
        }
    }

    private fun morph(node: Node<ActivationType, FeedbackType>) {
        if (node.isExtraInputRequested) {
            connectionsAdvisor.requestExtraConnection(node)?.also { addInput(node, it) }
        }
        node.inputsRemoveRequested.forEach { removeNodeId ->
            nodeIds.inverse()[removeNodeId]?.also { removeNode ->
                if (connectionsAdvisor.requestDropConnection(node, removeNode)) {
                    removeInput(node, removeNode)
                }
            }
        }
    }

    override fun touch(
        node: Node<ActivationType, FeedbackType>,
        feedbacks: List<FeedbackType>,
        inputs: Map<Int, ActivationType>
    ) {
        super.touch(node, feedbacks, inputs)
        morph(node)
    }
}