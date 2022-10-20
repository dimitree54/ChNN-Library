package we.rashchenko.chnn.network

import we.rashchenko.chnn.node.Node

class SelfMorphingNetwork<ActivationType, FeedbackType>(
    connectionsManagerBuilder: ConnectionsAdvisorBuilder<ActivationType, FeedbackType>
) : Network<ActivationType, FeedbackType>() {
    private val connectionsManager = connectionsManagerBuilder.build(graph)
    private fun addInput(
        node: Node<ActivationType, FeedbackType>,
        extraInput: Node<ActivationType, FeedbackType>
    ) {
        if (extraInput !in nodeIds) {
            val inputsForNewNode = connectionsManager.requestConnectionsForNewNode()
            if (inputsForNewNode.isEmpty()){
                return
            }
            nodeIds[extraInput] = getNextId()
            graph.addVertex(extraInput)
            inputsForNewNode.forEach { addInput(extraInput, it) }
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
        if (node.isExtraInputRequested()) {
            val extraInputs = connectionsManager.requestExtraConnection(node)
            extraInputs.forEach { addInput(node, it) }
        }
        node.inputsRemoveRequested().forEach { removeNodeId ->
            nodeIds.inverse()[removeNodeId]?.also { removeNode ->
                val drop = connectionsManager.requestDropConnection(node, removeNode)
                if (drop) {
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