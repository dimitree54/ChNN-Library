package we.rashchenko.chnn.environment

import we.rashchenko.chnn.node.Activity
import we.rashchenko.chnn.node.SmartNeuron

// todo should baseNode and externalActivity be protected? How to calculate loss then?
abstract class ExternallyControlledSmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>(
    val baseNode: SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>,
    val externalActivity: Activity<ActivationType?>,
) : SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType> by baseNode {
    override val activity: ActivationType
        get() = externalActivity.activity ?: baseNode.activity

    override fun listenForFeedbacks(feedbacks: Collection<FeedbackType>) {
        getExternalFeedback()?.let { baseNode.listenForFeedbacks(listOf(it)) } ?: baseNode.listenForFeedbacks(feedbacks)
    }

    abstract fun getExternalFeedback(): FeedbackType?
}