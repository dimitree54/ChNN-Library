package we.rashchenko.chnn.network.execution

import we.rashchenko.chnn.environment.Environment
import we.rashchenko.chnn.environment.EnvironmentExecutor
import we.rashchenko.chnn.network.ConnectionsAdvisor
import we.rashchenko.chnn.node.Activity
import we.rashchenko.chnn.node.SmartNeuron
import we.rashchenko.utility.Spawner
import we.rashchenko.utility.graph.AnonymousGraph
import we.rashchenko.utility.graph.DefaultMutableAnonymousGraph
import we.rashchenko.utility.id.BiAnonymizer
import we.rashchenko.utility.id.DefaultBiAnonymizer
import we.rashchenko.utility.id.SequentialIdGenerator

class SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType> {
    private lateinit var _neuralGraph: DefaultMutableAnonymousGraph<SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>
    val neuralGraph: AnonymousGraph<SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>
        get() = _neuralGraph

    private val executors = mutableListOf<Executor>()

    fun createNetwork(
        anonymizer: BiAnonymizer<SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>> = DefaultBiAnonymizer(
            SequentialIdGenerator()
        ),
    ): SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType> {
        _neuralGraph = DefaultMutableAnonymousGraph(anonymizer)
        return this
    }

    fun <EnvironmentActivationType>addEnvironment(
        environment: Environment<EnvironmentActivationType>,
        connectorsSpawner: Spawner<Activity<EnvironmentActivationType>, SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>,
    ): SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType> {
        environment.allNodes.forEach { environmentNode ->
            val connectorNode = connectorsSpawner.spawn(environmentNode)
            _neuralGraph.add(connectorNode)
        }
        executors.add(EnvironmentExecutor(environment))
        return this
    }

    fun addFeedForwardExecutor(): SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType> {
        val feedForwardExecutor = SimultaneousNeuralGraphExecutor(_neuralGraph)
        executors.add(feedForwardExecutor)
        return this
    }

    fun addBackPropagationExecutor(): SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType> {
        val feedbackExecutor = SimultaneousCollaborativeGraphExecutor(_neuralGraph)
        executors.add(feedbackExecutor)
        return this
    }

    fun <SpawnRequestType>addSelfMorphingExecutor(
        connectionsAdvisor: ConnectionsAdvisor<ConnectionRequestType, SpawnRequestType>,
        nodesSpawner: Spawner<SpawnRequestType, SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>,
    ): SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType> {
        val selfMorphingExecutor = SmartNeuronSelfConnectionGraphExecutor(_neuralGraph, connectionsAdvisor, nodesSpawner)
        executors.add(selfMorphingExecutor)
        return this
    }

    fun build(): Executor {
        return ComplexExecutor(*executors.toTypedArray())
    }
}