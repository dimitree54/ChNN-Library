package we.rashchenko.chnn.network

import we.rashchenko.chnn.node.Node
import we.rashchenko.chnn.node.NodeSpawner

class SelfMorphingNetwork<ActivationType, FeedbackType>(
    private val nodeSpawner: NodeSpawner<ActivationType, FeedbackType>,
    connectionsManagerBuilder: ConnectionsManagerBuilder<ActivationType, FeedbackType>
): Network<ActivationType, FeedbackType>()
{
    private val connectionsManager = connectionsManagerBuilder.build(graph)
    private fun morph(node: Node<ActivationType, FeedbackType>){
        if (node.isExtraInputRequested()){
            connectionsManager.requestExtraConnection(node)
        }
        node.inputsRemoveRequested().forEach {removeNodeId ->
            nodeIds.inverse()[removeNodeId]?.also{ removeNode ->
                connectionsManager.requestDropConnection(node, removeNode)
            }
        }
    }

    override fun touch(node: Node<ActivationType, FeedbackType>) {
        super.touch(node)
        morph(node)
    }
}