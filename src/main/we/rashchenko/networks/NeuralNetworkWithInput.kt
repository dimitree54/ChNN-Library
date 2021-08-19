package we.rashchenko.networks

import we.rashchenko.neurons.Neuron
import we.rashchenko.neurons.inputs.InputNeuron

/**
 * [NeuralNetwork] where some [Neuron]-nodes specially marked as [InputNeuron]. To understand what privileges [InputNeuron] has check its documentation.
 */
interface NeuralNetworkWithInput : NeuralNetwork {
	val inputNeuronIDs: Collection<Int>

	/**
	 * Add [InputNeuron] node to the graph.
	 * @return newly generated id for the added [InputNeuron].
	 * Now that [InputNeuron] will be referred with that id in [neuronIDs] and [connections]
	 */
	fun addInputNeuron(neuron: InputNeuron): Int
}