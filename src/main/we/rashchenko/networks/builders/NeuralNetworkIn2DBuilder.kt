package we.rashchenko.networks.builders

import we.rashchenko.base.Activity
import we.rashchenko.base.ObservableActivities
import we.rashchenko.networks.NeuralNetworkWithInput
import we.rashchenko.neurons.inputs.MirroringNeuron
import we.rashchenko.neurons.Neuron
import we.rashchenko.neurons.NeuronsSampler
import we.rashchenko.utils.KNearestVectorsConnectionSampler
import we.rashchenko.utils.RandomPositionSampler
import we.rashchenko.utils.Vector2
import we.rashchenko.utils.randomIds


/**
 * Builder that places [NeuralNetworkWithInput] into 2D space.
 * On each [addNeuron] coordinate for it sampled randomly from [0,1] and neuron
 *  connections sampled based on the distance between that coordinates.
 */
class NeuralNetworkIn2DBuilder(
	override val neuralNetwork: NeuralNetworkWithInput,
	private val neuronsSampler: NeuronsSampler
) : NeuralNetworkWithInputBuilder {
	private val positionSampler: Iterator<Vector2> = RandomPositionSampler()
	private val vectorsConnectionSampler = KNearestVectorsConnectionSampler(5)
	private val positionsWithNNIDs = mutableMapOf<Vector2, Int>()
	private val nnIDsWithPosition = mutableMapOf<Int, Vector2>()

	// Note that because of privacy we can not use the same ids as in NN
	private val nnIDs2builderIDs = mutableMapOf<Int, Int>()

	private val unattachedActivitiesWithPosition = mutableListOf<Pair<Activity, Vector2>>()
	private fun addNeuronWithoutConnection(): Int {
		val builderID = randomIds.next()
		val neuron: Neuron = sample(builderID)
		return if (unattachedActivitiesWithPosition.isEmpty()) {
			neuralNetwork.add(neuron).also { neuronID ->
				addNeuronWithoutConnection(neuronID, builderID, positionSampler.next())
			}
		} else {
			val (activity, position) = unattachedActivitiesWithPosition.removeLast()
			neuralNetwork.addInputNeuron(MirroringNeuron(activity, neuron)).also { neuronID ->
				addNeuronWithoutConnection(neuronID, builderID, position)
				neuronIDsConnectedToActivity[neuronID] = activity
			}
		}

	}

	private fun addNeuronWithoutConnection(neuronID: Int, builderID: Int, position: Vector2) {
		positionsWithNNIDs[position] = neuronID
		nnIDsWithPosition[neuronID] = position
		nnIDs2builderIDs[neuronID] = builderID
	}

	override fun addNeuron(): Int =
		addNeuronWithoutConnection().also { neuronID ->
			connect(nnIDsWithPosition[neuronID]!!)
		}

	private val environmentIDsWithNeuronIDs = mutableMapOf<Int, List<Int>>()
	private val neuronIDsConnectedToActivity = mutableMapOf<Int, Activity>()
	private fun addEnvironmentWithoutConnection(environment: ObservableActivities): List<Int> {
		val neuronIDs = mutableListOf<Int>()
		environment.activities.associateWith { positionSampler.next() }
			.forEach { (activity, position) ->
				val builderID = randomIds.next()
				val neuron = MirroringNeuron(activity, sample(builderID))
				val nnID = neuralNetwork.addInputNeuron(neuron)
				addNeuronWithoutConnection(nnID, builderID, position)

				neuronIDsConnectedToActivity[nnID] = activity
				neuronIDs.add(nnID)
			}
		return neuronIDs
	}

	override fun addEnvironment(environment: ObservableActivities): Int {
		val inputNeurons = addEnvironmentWithoutConnection(environment)
		inputNeurons.forEach { neuronID ->
			connect(nnIDsWithPosition[neuronID]!!)
		}
		val environmentID = randomIds.next()
		environmentIDsWithNeuronIDs[environmentID] = inputNeurons
		return environmentID
	}

	override fun removeEnvironment(environmentID: Int): Boolean {
		environmentIDsWithNeuronIDs[environmentID]?.forEach {
			neuronIDsConnectedToActivity.remove(it)
		} ?: return false
		environmentIDsWithNeuronIDs.remove(environmentID)
		return true
	}

	override fun remove(neuronID: Int): Boolean {
		return neuralNetwork.remove(neuronID).also { removed ->
			if (removed) {
				val builderID = nnIDs2builderIDs[neuronID]!!
				val position = nnIDsWithPosition[neuronID]!!
				if (neuronID in neuronIDsConnectedToActivity) {
					unattachedActivitiesWithPosition.add(
						neuronIDsConnectedToActivity[neuronID]!! to nnIDsWithPosition[neuronID]!!
					)
					neuronIDsConnectedToActivity.remove(neuronID)
				}

				neuronsSampler.reportDeath(builderID)
				positionsWithNNIDs.remove(position)
				nnIDsWithPosition.remove(neuronID)
				nnIDs2builderIDs.remove(neuronID)
			}
		}
	}

	private fun connect(position: Vector2) {
		val newConnections = vectorsConnectionSampler.connectNew(position, nnIDsWithPosition.values)
		newConnections.forEach { connectedPosition ->
			neuralNetwork.addConnection(
				positionsWithNNIDs[position]!!, positionsWithNNIDs[connectedPosition]!!
			)
			neuralNetwork.addConnection(
				positionsWithNNIDs[connectedPosition]!!, positionsWithNNIDs[position]!!
			)
		}
	}

	/**
	 * The function that reveals 2d coordinates of neurons. Useful for visualisations.
	 */
	fun getPosition(neuronID: Int): Vector2? = nnIDsWithPosition[neuronID]

	private var lastSamplerUpdateTimeStep = -1L
	private fun sample(id: Int): Neuron{
		if (lastSamplerUpdateTimeStep != neuralNetwork.timeStep){
			lastSamplerUpdateTimeStep = neuralNetwork.timeStep
			updateSampler()
		}
		return neuronsSampler.next(id)
	}

	private fun updateSampler() {
		nnIDs2builderIDs.forEach{ (neuronID, builderID) ->
			neuronsSampler.reportFeedback(builderID, neuralNetwork.getFeedback(neuronID)!!)
		}
	}
}
