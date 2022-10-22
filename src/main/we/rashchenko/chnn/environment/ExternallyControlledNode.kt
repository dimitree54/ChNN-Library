package we.rashchenko.chnn.environment

import we.rashchenko.chnn.node.Node

abstract class ExternallyControlledNode<ActivationType, FeedbackType>(
    protected val baseNode: Node<ActivationType, FeedbackType>
) :
    Node<ActivationType, FeedbackType> by baseNode {
    abstract var externalActivity: ActivationType?

    override val activity: ActivationType
        get() = externalActivity ?: baseNode.activity
    val baseActivity: ActivationType
        get() = baseNode.activity

    override fun reportFeedbacks(feedbacks: List<FeedbackType>) {
        getExternalFeedback()?.let { baseNode.reportFeedbacks(listOf(it)) } ?: baseNode.reportFeedbacks(feedbacks)
    }

    abstract fun getExternalFeedback(): FeedbackType?
}