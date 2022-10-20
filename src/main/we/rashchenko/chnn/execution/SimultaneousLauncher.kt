package we.rashchenko.chnn.execution

import we.rashchenko.chnn.network.Network

class SimultaneousLauncher<ActivationType, FeedbackType>(
    private val network: Network<ActivationType, FeedbackType>,
) {

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
}