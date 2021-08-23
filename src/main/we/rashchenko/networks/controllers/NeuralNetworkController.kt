package we.rashchenko.networks.controllers

import we.rashchenko.base.Feedback
import we.rashchenko.networks.NeuralNetwork
import we.rashchenko.neurons.ControlledNeuron

/**
 * Interface for the classes that can calculate external [Feedback] knowing stats of all
 *  [NeuralNetwork] [ControlledNeuron]s/.
 */
interface NeuralNetworkController {
	fun getControllerFeedbacks(neurons: List<ControlledNeuron>, timeStep: Long): List<Feedback>
}
