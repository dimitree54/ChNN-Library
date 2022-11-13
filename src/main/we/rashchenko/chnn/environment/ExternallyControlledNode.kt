package we.rashchenko.chnn.environment

import we.rashchenko.chnn.node.Activity
import we.rashchenko.chnn.node.SmartNeuron

// todo is it ok that ExternallyControlledNode implements SmartNeuron, but ConnectionRequestType is not used?
abstract class ExternallyControlledNode<ActivationType, FeedbackType, ConnectionRequestType>(
    protected val baseNode: SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>,
    protected val externalActivity: Activity<ActivationType?>
) : SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType> by baseNode {
    override val activity: ActivationType
        get() = externalActivity.activity ?: baseNode.activity

    override fun listenForFeedbacks(feedbacks: Collection<FeedbackType>) {
        getExternalFeedback()?.let { baseNode.listenForFeedbacks(listOf(it)) } ?: baseNode.listenForFeedbacks(feedbacks)
    }

    abstract fun getExternalFeedback(): FeedbackType?
}