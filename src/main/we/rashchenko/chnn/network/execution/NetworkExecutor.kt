package we.rashchenko.chnn.network.execution

import we.rashchenko.chnn.network.ConnectionsAdvisor
import we.rashchenko.chnn.node.CollaborativeUnit
import we.rashchenko.chnn.node.Neuron
import we.rashchenko.chnn.node.SelfConnectingUnit
import we.rashchenko.utility.Spawner
import we.rashchenko.utility.Ticking
import we.rashchenko.utility.graph.DirectedGraph

interface GraphExecutor<NodeType> : Ticking

class ComplexGraphExecutor<NodeType>(
    vararg executors: GraphExecutor<NodeType>,
) : GraphExecutor<NodeType> {
    private val executors = executors.toList()
    override fun tick() {
        executors.forEach { it.tick() }
    }
}

abstract class NeuralGraphExecutor<ActivationType, NodeType: Neuron<ActivationType>>(
    protected val neuralGraph: DirectedGraph<NodeType>,
    private val anonymizer: Anonymizer<NodeType>,
) : GraphExecutor<NodeType> {
    protected fun gatherInputs(node: NodeType) =
        neuralGraph.getInputsOf(node).associateWith { it.activity }

    private fun anonymizeInputs(inputs: Map<NodeType, ActivationType>) =
        inputs.mapKeys { anonymizer.anonymize(it.key) }

    protected fun touch(node: Neuron<ActivationType>, inputs: Map<NodeType, ActivationType>) {
        node.touch(anonymizeInputs(inputs))
    }
}

class SimultaneousNeuralGraphExecutor<ActivationType, NodeType: Neuron<ActivationType>>(
    neuralGraph: DirectedGraph<NodeType>,
    anonymizer: Anonymizer<NodeType>,
) : NeuralGraphExecutor<ActivationType, NodeType>(neuralGraph, anonymizer) {
    private fun gatherAllInputs() = neuralGraph.allNodes.associateWith { gatherInputs(it) }
    override fun tick() {
        val allInputs = gatherAllInputs()
        neuralGraph.allNodes.forEach { node ->
            touch(node, allInputs[node]!!)
        }
    }
}

abstract class CollaborativeGraphExecutor<FeedbackType, in NodeType: CollaborativeUnit<FeedbackType>>(
    protected val neuralGraph: DirectedGraph<NodeType>,
    private val anonymizer: Anonymizer<NodeType>,
) : GraphExecutor<CollaborativeUnit<FeedbackType>> {
    protected fun gatherFeedbacks(node: NodeType): List<FeedbackType> {
        val nodeId = anonymizer.anonymize(node)
        return neuralGraph.getListenersOf(node).mapNotNull { it.feedbacks[nodeId] }
    }

    protected fun reportFeedbacks(node: NodeType, feedbacks: Collection<FeedbackType>) =
        node.listenForFeedbacks(feedbacks)
}

class SimultaneousCollaborativeGraphExecutor<FeedbackType, NodeType: CollaborativeUnit<FeedbackType>>(
    neuralGraph: DirectedGraph<NodeType>,
    anonymizer: Anonymizer<NodeType>,
) : CollaborativeGraphExecutor<FeedbackType, NodeType>(neuralGraph, anonymizer) {
    private fun gatherAllFeedbacks() = neuralGraph.allNodes.associateWith { gatherFeedbacks(it) }
    override fun tick() {
        val allFeedbacks = gatherAllFeedbacks()
        neuralGraph.allNodes.forEach { node ->
            reportFeedbacks(node, allFeedbacks[node]!!)
        }
    }
}

abstract class SelfConnectingGraphExecutor<ConnectionRequestType, NodeType: SelfConnectingUnit<ConnectionRequestType>>(
    protected val neuralGraph: DirectedGraph<NodeType>,
    private val anonymizer: Anonymizer<NodeType>,
    private val connectionsAdvisor: ConnectionsAdvisor<ConnectionRequestType, NodeType>,
    private val nodesSpawner: Spawner<ConnectionRequestType, NodeType>,
) : Ticking {
    private fun removeInput(
        requestingNode: NodeType,
        inputNodeToRemove: NodeType,
    ) {
        neuralGraph.cut(inputNodeToRemove, requestingNode)
        if (neuralGraph.getListenersOf(inputNodeToRemove).isEmpty()) {
            neuralGraph.remove(inputNodeToRemove)
        }
    }
    protected fun removeInputsIfRequested(node: NodeType) {
        node.inputsRemoveRequest.forEach {
            val nodeToRemove = anonymizer.deAnonymize(it)
            if (connectionsAdvisor.requestRemoveInput(node, nodeToRemove)) {
                removeInput(node, nodeToRemove)
            }
        }
    }
    private fun spawnAndConnectToNode(
        node: NodeType,
        spawnRequest: ConnectionRequestType
    ): Boolean{
        val newNode = nodesSpawner.spawn(spawnRequest)
        neuralGraph.add(newNode)
        return if (addInput(newNode, spawnRequest)) {
            neuralGraph.wire(node, newNode)
            true
        } else {
            neuralGraph.remove(newNode)
            false
        }
    }
    private fun addInput(node: NodeType, request: ConnectionRequestType): Boolean {
        val advice = connectionsAdvisor.requestExtraInput(request, node)
        if (advice.spawn) {
            return spawnAndConnectToNode(node, advice.spawnRequest!!)
        } else if (advice.addInputFromExistingNode) {
            neuralGraph.wire(node, advice.addInputFrom!!)
            return true
        }
        return false
    }

    protected fun addInputIfRequested(node: NodeType) {
        addInput(node, node.extraInputRequest ?: return)
    }
}

class DefaultSelfConnectingGraphExecutor<ConnectionRequestType, NodeType: SelfConnectingUnit<ConnectionRequestType>>(
    neuralGraph: DirectedGraph<NodeType>,
    anonymizer: Anonymizer<NodeType>,
    connectionsAdvisor: ConnectionsAdvisor<ConnectionRequestType, NodeType>,
    nodesSpawner: Spawner<ConnectionRequestType, NodeType>
) : SelfConnectingGraphExecutor<ConnectionRequestType, NodeType>(neuralGraph, anonymizer, connectionsAdvisor, nodesSpawner) {
    override fun tick() {
        neuralGraph.allNodes.forEach { node ->
            removeInputsIfRequested(node)
            addInputIfRequested(node)
        }
    }
}