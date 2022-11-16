package we.rashchenko.chnn.network.execution

import we.rashchenko.chnn.node.Neuron
import we.rashchenko.utility.graph.AnonymousGraph

abstract class NeuralGraphExecutor<ActivationType>(
    protected val neuralGraph: AnonymousGraph<Neuron<ActivationType>>,
) : GraphExecutor {
    protected fun gatherInputs(id: Int) =
        neuralGraph.getInputsOf(id).associateWith { neuralGraph.deAnonymize(it).activity }

    protected fun touch(id: Int, inputs: Map<Int, ActivationType>) = neuralGraph.deAnonymize(id).touch(inputs)
}