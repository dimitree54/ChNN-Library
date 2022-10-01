package we.rashchenko.chnn.network

import we.rashchenko.chnn.node.Node
import we.rashchenko.chnn.node.NodeSpawner

class SelfMorphingNetwork<ActivationType, FeedbackType>(
    private val nodeSpawner: NodeSpawner<ActivationType, FeedbackType>,
    connectionsManagerBuilder: ConnectionsManagerBuilder<ActivationType, FeedbackType>
): Network<ActivationType, FeedbackType>()
{
    private val connectionsManager = connectionsManagerBuilder.build(graph)
    fun morph(node: Node<ActivationType, FeedbackType>){
        if (node.isExtraInputRequested()){
            connectionsManager.requestExtraConnection(node)
        }
        node.inputsRemoveRequested().forEach {
            connectionsManager.requestDropConnection(node, )
        }
    }
}