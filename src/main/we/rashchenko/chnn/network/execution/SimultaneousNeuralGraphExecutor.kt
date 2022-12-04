package we.rashchenko.chnn.network.execution

import we.rashchenko.chnn.neuron.Neuron
import we.rashchenko.utility.graph.AnonymousGraph

class SimultaneousNeuralGraphExecutor<ActivationType>(
    neuralGraph: AnonymousGraph<Neuron<ActivationType>>,
) : NeuralGraphExecutor<ActivationType>(neuralGraph) {
    private fun gatherAllInputs() = neuralGraph.allIds.associateWith { gatherInputs(it) }
    override fun tick() = gatherAllInputs().forEach { (id, inputs) ->
        touch(id, inputs)
    }
}