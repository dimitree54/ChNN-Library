package we.rashchenko.networks

import we.rashchenko.base.Feedback
import we.rashchenko.networks.builders.Evolution
import we.rashchenko.networks.controllers.NeuralNetworkController
import we.rashchenko.neurons.ControlledNeuron
import we.rashchenko.neurons.Neuron
import we.rashchenko.neurons.inputs.InputNeuron
import java.util.*

/**
 * That [NeuralNetwork] is a wrapper for any other [baseNeuralNetwork] that wraps all newly added [Neuron]
 *  with [ControlledNeuron] in order to sometimes (with [auditProbability]) audit [Neuron] behaviour.
 * We do it only sometimes because such audit increase [Neuron] touch time which for big amount of neurons may slow down
 * [tick] for [NeuralNetwork].
 * So, choose [auditProbability] based on what [controller] you use.
 * Based on that audit results for all the neurons [controller] can use that global information to estimate neurons'
 *  relative quality (lets name that type of feedback as external).
 * [ControlledNeuralNetwork] not only wraps all the neurons with [ControlledNeuron], but also changes the [getFeedback]
 *  behaviour.
 * [ControlledNeuralNetwork] replaces [getFeedback] of the [baseNeuralNetwork] (lets call it internal feedback)
 *  with the weighted sum of internal and external feedback.
 * The weight of the sum determined by [controllerFeedbackWeight].
 * Note that external [Feedback] have to be calculated for all neurons in the [NeuralNetwork] (even for not active),
 *  so it is even more expensive than [ControlledNeuron] audit (which is called only for active neurons).
 * To not let it slow down each [tick] that external feedback for all neurons collected not on each tick,
 *  but periodically (with [updateControllerFeedbackPeriod] period).
 */
class ControlledNeuralNetwork(
	private val baseNeuralNetwork: NeuralNetworkWithInput,
	private val controller: NeuralNetworkController,
	private val auditProbability: Double,
	private val updateControllerFeedbackPeriod: Long,
	private val controllerFeedbackWeight: Double
) : NeuralNetworkWithInput by baseNeuralNetwork {
	private val controlledNeuronsWithID = mutableMapOf<Int, ControlledNeuron>()
	private val controllerFeedbacks = mutableMapOf<Int, Feedback>()

	override fun add(neuron: Neuron): Int {
		return ControlledNeuron(neuron, timeStep).let {
			baseNeuralNetwork.add(it).also { id ->
				controlledNeuronsWithID[id] = it
				controllerFeedbacks[id] = Feedback.NEUTRAL
			}
		}
	}

	override fun addInputNeuron(neuron: InputNeuron): Int {
		// NOTE: we do not control input neurons as they anyway ignore external feedback, just specifying default
		return baseNeuralNetwork.addInputNeuron(neuron).also { neuronID ->
			controllerFeedbacks[neuronID] = Feedback.NEUTRAL
		}
	}

	override fun remove(neuronID: Int): Boolean {
		return baseNeuralNetwork.remove(neuronID).also { removed ->
			if (removed) {
				controlledNeuronsWithID.remove(neuronID)
				controllerFeedbacks.remove(neuronID)
			}
		}
	}

	private val random = Random()
	private var control = false
	override fun tick() {
		if (control) {
			control = false
			controlledNeuronsWithID.values.forEach { it.control = false }
		}
		if (random.nextDouble() < auditProbability) {
			control = true
			controlledNeuronsWithID.values.forEach { it.control = true }
		}
		baseNeuralNetwork.tick()
		if (timeStep % updateControllerFeedbackPeriod == 0L) {
			val (neuronIDsList, controlledNeuronsList) = controlledNeuronsWithID.toList().unzip()
			val feedbacks = controller.getControllerFeedbacks(controlledNeuronsList, timeStep)
			neuronIDsList.forEachIndexed { i, id ->
				controllerFeedbacks[id] = feedbacks[i]
			}
		}
	}

	override fun getFeedback(neuronID: Int): Feedback? {
		return getExternalFeedback(neuronID)?.let { controllerFeedback ->
			getInternalFeedback(neuronID)?.let { collaborativeFeedback ->
				Feedback(
					collaborativeFeedback.value * (1 - controllerFeedbackWeight) +
							controllerFeedback.value * controllerFeedbackWeight
				)
			}
		}
	}

	/**
	 * Since [getFeedback] now returns weighted sum of internal and external feedbacks, we need [getExternalFeedback] and [getInternalFeedback] to get original values of external and internal feedback. We need that mainly for logging and visualisation purposes, that functions are not used by [Evolution].
	 */
	fun getExternalFeedback(neuronID: Int): Feedback? = controllerFeedbacks[neuronID]

	/**
	 * Since [getFeedback] now returns weighted sum of internal and external feedbacks, we need [getExternalFeedback] and [getInternalFeedback] to get original values of external and internal feedback. We need that mainly for logging and visualisation purposes, that functions are not used by [Evolution].
	 */
	fun getInternalFeedback(neuronID: Int): Feedback? = baseNeuralNetwork.getFeedback(neuronID)
}
