package we.rashchenko.chnn.network

import we.rashchenko.chnn.node.Node

class SelfMorphingNetwork<ActivationType, FeedbackType>(
    connectionsManagerBuilder: ConnectionsAdvisorBuilder<ActivationType, FeedbackType>
): Network<ActivationType, FeedbackType>()
{
    private val connectionsManager = connectionsManagerBuilder.build(graph)
    private fun morph(node: Node<ActivationType, FeedbackType>){
        if (node.isExtraInputRequested()){
            val extraInputs = connectionsManager.requestExtraConnection(node)
            extraInputs.forEach {
                if (it !in nodeIds){
                    nodeIds[it] = getNextId()
                    graph.addVertex(it)
                }
                node.addInput(nodeIds[it]!!)
                graph.addEdge(it, node)
            }
        }
        node.inputsRemoveRequested().forEach {removeNodeId ->
            nodeIds.inverse()[removeNodeId]?.also{ removeNode ->
                val dropped = connectionsManager.requestDropConnection(node, removeNode)
                if (dropped){
                    node.removeInput(removeNodeId)
                    graph.removeEdge(removeNode, node)
                    if (graph.inDegreeOf(removeNode) == 0){
                        removeNode(removeNode)
                        graph.removeVertex(removeNode)
                    }
                }
            }
        }
    }

    override fun touch(nodeId: Int, feedbacks: List<FeedbackType>, inputs: Map<Int, ActivationType>){
        super.touch(nodeId, feedbacks, inputs)
        morph(nodeIds.inverse()[nodeId]!!)
    }
}