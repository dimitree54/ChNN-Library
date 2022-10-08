package we.rashchenko.chnn.environment

import we.rashchenko.chnn.node.Node

abstract class ExternallyControlledNode<ActivationType, FeedbackType>(protected val baseNode: Node<ActivationType, FeedbackType>) :
    Node<ActivationType, FeedbackType> by baseNode {
    var externalActivity: ActivationType? = null

    override fun getActivation(): ActivationType {
        return externalActivity ?: baseNode.getActivation()
    }

    override fun reportFeedbacks(feedbacks: List<FeedbackType>) {
        baseNode.reportFeedbacks(listOf(getExternalFeedback()))
    }

    abstract fun getExternalFeedback(): FeedbackType
}