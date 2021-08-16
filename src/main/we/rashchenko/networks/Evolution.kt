package we.rashchenko.networks

import we.rashchenko.base.Feedback
import we.rashchenko.base.Ticking
import we.rashchenko.networks.builders.NeuralNetworkBuilder
import we.rashchenko.neurons.Neuron
import we.rashchenko.utils.collections.WorstNNeuronIDs
import java.util.*

/**
 * The class to provide evolution for neurons in [NeuralNetwork].
 * [Evolution] is ticking independently of target [NeuralNetwork] and sometimes
 *  (with [selectionProbability]) collects [Feedback] for all the neurons in the [NeuralNetwork].
 * [Feedback] for non-active neurons also collected which makes that operation quite expensive,
 *  that is why we do it just sometimes.
 * During that [Feedback] collection [builder] notified about each [Neuron] [Feedback] by reportFeedback, so [builder] knows what neurons are more successful.
 * After collecting [Feedback] for all neurons the worst [neuronsForSelection] of them considered as candidates for removing.
 * That neurons are notified by update function (that update considered as warning).
 * If some [Neuron] warned [warningsBeforeKill] (in total, not only consequently) it is replaced with other random
 *  [Neuron] by calling remove and add functions of the [builder].
 * So, as you can see that class manages only bad neurons killing,
 *  but sampling of new successful neurons is managed by [builder].
 */
class Evolution(
	private val builder: NeuralNetworkBuilder,
	private val neuronsForSelection: Int,
	private val warningsBeforeKill: Int,
	private val selectionProbability: Double
): Ticking {
	private val warnings = mutableMapOf<Int, Int>()
	private val random = Random()
	override var timeStep: Long = 0
		private set
	override fun tick() {
		if (random.nextDouble() > selectionProbability) {
			return
		}
		val losers = WorstNNeuronIDs(neuronsForSelection)
		builder.neuralNetwork.neuronIDs.forEach { neuronID ->
			val neuronFeedback = builder.neuralNetwork.getFeedback(neuronID)!!
			builder.reportFeedback(neuronID, neuronFeedback)
			losers.add(Pair(neuronID, neuronFeedback))
		}
		losers.forEach { (neuronID, feedback) ->
			val newWarningsValue = warnings.getOrDefault(neuronID, 0) + 1
			warnings[neuronID] = newWarningsValue
			if (newWarningsValue > warningsBeforeKill) {
				if (builder.remove(neuronID)) {
					builder.addNeuron()
				} else {
					// warning for the bad neuron
					builder.neuralNetwork.getNeuron(neuronID)!!.update(feedback, builder.neuralNetwork.timeStep)
				}
			}
		}
		timeStep++
	}
}