package we.rashchenko.chnn.execution

import we.rashchenko.chnn.environment.Environment
import we.rashchenko.chnn.environment.ExternallyControlledNode
import we.rashchenko.chnn.network.Network

class SimultaneousLauncher<ActivationType, FeedbackType, ConnectionRequestType>(
    private val network: Network<ActivationType, FeedbackType, ConnectionRequestType>,
    private val environment: Environment<ActivationType>,
    private val connectors: List<ExternallyControlledNode<ActivationType, FeedbackType, ConnectionRequestType>>
) {

    init {
        updateConnectors()
    }

    private fun updateConnectors() {
        connectors.zip(environment.getState()).forEach { (connector, state) ->
            connector.externalActivity = state
        }
    }

    private fun gatherAllInputs(): Map<Int, Map<Int, ActivationType>>{
        return network.getAllIds().associateWith { network.gatherInputs(it) }
    }

    private fun gatherAllFeedbacks(): Map<Int, List<FeedbackType>>{
        return network.getAllIds().associateWith { network.gatherFeedbacks(it) }
    }

    fun tick(){
        val allInputs = gatherAllInputs()
        val allFeedbacks = gatherAllFeedbacks()
        network.getAllIds().forEach { nodeId ->
            network.touch(nodeId, allFeedbacks[nodeId]!!, allInputs[nodeId]!!)
        }
    }

    fun update(){
        network.update()
        environment.tick()
        updateConnectors()
    }
}