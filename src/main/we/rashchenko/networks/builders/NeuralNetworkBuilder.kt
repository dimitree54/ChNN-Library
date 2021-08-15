package we.rashchenko.networks.builders

import we.rashchenko.base.Feedback
import we.rashchenko.networks.NeuralNetwork

interface NeuralNetworkBuilder {
	val neuralNetwork: NeuralNetwork
	fun addNeuron(): Int
	fun remove(neuronID: Int): Boolean
	fun reportFeedback(neuronID: Int, feedback: Feedback)
}