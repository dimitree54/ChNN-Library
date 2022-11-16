package we.rashchenko.chnn.network.execution

import we.rashchenko.chnn.network.ConnectionsAdvisor
import we.rashchenko.chnn.node.SmartNeuron
import we.rashchenko.utility.Spawner
import we.rashchenko.utility.graph.MutableAnonymousGraph

class SmartNeuronSelfConnectionGraphExecutor<ActivationType, FeedbackType, ConnectionRequestType, SpawnRequestType>(
    private val neuralGraph: MutableAnonymousGraph<SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>,
    private val connectionsAdvisor: ConnectionsAdvisor<ConnectionRequestType, SpawnRequestType, SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>,
    private val nodesSpawner: Spawner<SpawnRequestType, SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>,
) : GraphExecutor {
    private fun removeInput(
        requestingId: Int,
        inputIdToRemove: Int,
    ) {
        neuralGraph.cut(inputIdToRemove, requestingId)
        neuralGraph.deAnonymize(requestingId).forgetInput(inputIdToRemove)
        if (neuralGraph.getListenersOf(inputIdToRemove).isEmpty()) {
            neuralGraph.remove(inputIdToRemove)
        }
    }

    private fun removeInputsIfRequested(id: Int) {
        val requestingNode = neuralGraph.deAnonymize(id)
        requestingNode.inputsRemoveRequest.forEach {
            if (connectionsAdvisor.requestRemoveInput(requestingNode, neuralGraph.deAnonymize(it))) {
                removeInput(id, it)
            }
        }
    }

    private fun spawnAndConnectToNode(
        id: Int,
        newNodeConnectionRequest: ConnectionRequestType,
        spawnRequest: SpawnRequestType,
    ): Boolean {
        val newNode = nodesSpawner.spawn(spawnRequest)
        val newNodeId = neuralGraph.add(newNode)
        neuralGraph.add(newNode)
        return if (addInput(newNodeId, newNodeConnectionRequest)) {
            neuralGraph.wire(id, newNodeId)
            true
        } else {
            neuralGraph.remove(newNodeId)
            false
        }
    }

    private fun addInput(id: Int, request: ConnectionRequestType): Boolean {
        val advice = connectionsAdvisor.requestExtraInput(request, neuralGraph.deAnonymize(id))
        if (advice.newNodeRequests != null) {
            return spawnAndConnectToNode(id, advice.newNodeRequests.first, advice.newNodeRequests.second)
        } else if (advice.addInputFrom != null) {
            neuralGraph.wire(id, neuralGraph.anonymize(advice.addInputFrom))
            return true
        }
        return false
    }

    private fun addInputIfRequested(id: Int) {
        addInput(id, neuralGraph.deAnonymize(id).extraInputRequest ?: return)
    }

    override fun tick() = neuralGraph.allIds.forEach { id ->
        removeInputsIfRequested(id)
        addInputIfRequested(id)
    }
}