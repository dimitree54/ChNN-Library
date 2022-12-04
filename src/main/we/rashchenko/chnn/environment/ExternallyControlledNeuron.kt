package we.rashchenko.chnn.environment

import we.rashchenko.chnn.neuron.Activity
import we.rashchenko.chnn.neuron.BalancingSmartNeuron

abstract class ExternallyControlledNeuron<EnvironmentActivationType, NeuronActivationType, FeedbackType, ConnectionRequestType>(
    private val externalActivity: Activity<EnvironmentActivationType>,
    private val baseNeuron: BalancingSmartNeuron<NeuronActivationType, FeedbackType, ConnectionRequestType>,
) : BalancingSmartNeuron<NeuronActivationType, FeedbackType, ConnectionRequestType> by baseNeuron {

    override val activity: NeuronActivationType
        get() = fuseExternalActivityIntoNeuron(externalActivity.activity, baseNeuron.activity)

    protected abstract fun fuseExternalActivityIntoNeuron(
        externalActivity: EnvironmentActivationType, neuronActivity: NeuronActivationType,
    ): NeuronActivationType

    override fun listenForFeedbacks(feedbacks: Collection<FeedbackType>) {
        baseNeuron.listenForFeedbacks(listOf(getExternalFeedback(externalActivity.activity, baseNeuron.activity)))
    }

    protected abstract fun getExternalFeedback(
        externalActivity: EnvironmentActivationType, neuronActivity: NeuronActivationType,
    ): FeedbackType

    override fun toString(): String {
        return "$externalActivity ${super.toString()}"
    }

    val externalActivation: EnvironmentActivationType
        get() = externalActivity.activity

    val internalActivation: NeuronActivationType
        get() = baseNeuron.activity
}

