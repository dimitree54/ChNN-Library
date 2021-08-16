package we.rashchenko.neurons.inputs

import we.rashchenko.base.Activity
import we.rashchenko.base.Feedback
import we.rashchenko.neurons.Neuron


// Other external neurons possible:
//  - in that one external activity is dominating. That is good for receiving always available external activity,
//     by bad for training on sometimes missing activity.
//  - other option is to make internal activity dominating and make external activity only affecting feedback,
//     that is good for training, but how initial activity will appear at network?
class MirroringNeuron(
	private val externalActivity: Activity, private val baseNeuron: Neuron
) : InputNeuron {
	override fun update(feedback: Feedback, timeStep: Long) = baseNeuron.update(getInternalFeedback(), timeStep)

	override fun getInternalFeedback(): Feedback =
		if (externalActivity.active == baseNeuron.active) Feedback.VERY_POSITIVE else Feedback.VERY_NEGATIVE

	override fun touch(sourceId: Int, timeStep: Long) = baseNeuron.touch(sourceId, timeStep)
	override fun forgetSource(sourceId: Int) = baseNeuron.forgetSource(sourceId)
	override fun getFeedback(sourceId: Int): Feedback = baseNeuron.getFeedback(sourceId)

	override val active: Boolean
		get() = externalActivity.active
}