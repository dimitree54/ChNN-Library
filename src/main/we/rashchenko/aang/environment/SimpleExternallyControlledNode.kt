package we.rashchenko.aang.environment

import we.rashchenko.chnn.environment.ExternallyControlledNode
import we.rashchenko.chnn.node.Node

class SimpleExternallyControlledNode(baseNode: Node<Boolean, Boolean>) :
    ExternallyControlledNode<Boolean, Boolean>(
        baseNode
    ) {
    override fun getExternalFeedback(): Boolean {
        return externalActivity == baseNode.getActivation()
    }
}