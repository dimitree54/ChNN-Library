package we.rashchenko.chnn.environment

import we.rashchenko.chnn.node.Activity
import we.rashchenko.chnn.node.Node

abstract class EnvironmentConnectorNode<ActivityType, FeedbackType>(
    private val environmentActivity: Activity<ActivityType>
) : Node<ActivityType, FeedbackType> {
    abstract fun calculateFeedback(
        environmentActivity: ActivityType,
        nodeActivity: ActivityType
    ): FeedbackType

    override fun getFeedbacks(): Map<Int, FeedbackType> {
        if (inputId != null && lastNodeActivity != null){
            return mapOf(inputId!! to calculateFeedback(environmentActivity.getActivation(), lastNodeActivity!!))
        }
        return mapOf()
    }

    private var lastNodeActivity: ActivityType? = null
    override fun touch(inputs: Map<Int, ActivityType>) {
        if (inputId in inputs){
            lastNodeActivity = inputs[inputId]
        }
    }

    override fun reportFeedbacks(feedbacks: List<FeedbackType>) {}

    private var inputId: Int? = null
    override fun addInput(inputId: Int) {
        this.inputId = inputId
    }

    override fun removeInput(inputId: Int) {}

    override fun getActivation() = environmentActivity.getActivation()

    override fun isExtraInputRequested() = false

    override fun inputsRemoveRequested() = emptyList<Int>()
}