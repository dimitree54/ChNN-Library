package we.rashchenko.chnn.network.execution

import we.rashchenko.chnn.network.ConnectionsAdvisor
import we.rashchenko.chnn.neuron.SmartNeuron
import we.rashchenko.utility.Spawner
import we.rashchenko.utility.graph.MutableAnonymousGraph

class SmartNeuronSelfConnectionGraphExecutor<ConnectionRequestType, SpawnRequestType, NodeType: SmartNeuron<*, *, ConnectionRequestType>>(
    private val neuralGraph: MutableAnonymousGraph<NodeType>,
    private val connectionsAdvisor: ConnectionsAdvisor<ConnectionRequestType, SpawnRequestType>,
    private val nodesSpawner: Spawner<SpawnRequestType, NodeType>,
) : Executor {
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
        neuralGraph.deAnonymize(id).inputsRemoveRequest.forEach {
            if (connectionsAdvisor.requestRemoveInput(id, it)) {
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
            neuralGraph.wire(newNodeId, id)
            true
        } else {
            neuralGraph.remove(newNodeId)
            false
        }
    }

    private fun addInput(id: Int, request: ConnectionRequestType): Boolean {
        val advice = connectionsAdvisor.requestExtraInput(request, id)
        if (advice.newNodeRequests != null) {
            return spawnAndConnectToNode(id, advice.newNodeRequests.first, advice.newNodeRequests.second)
        } else if (advice.addInputFrom != null) {
            neuralGraph.wire(advice.addInputFrom, id)
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