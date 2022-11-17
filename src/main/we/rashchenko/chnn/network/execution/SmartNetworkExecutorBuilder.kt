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

class SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType, SpawnRequestType> {
    private lateinit var _neuralGraph: DefaultMutableAnonymousGraph<SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>
    val neuralGraph: AnonymousGraph<SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>
        get() = _neuralGraph

    private val executors = mutableListOf<Executor>()

    fun createNetwork(
        anonymizer: BiAnonymizer<SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>> = DefaultBiAnonymizer(
            SequentialIdGenerator()
        ),
    ): SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType, SpawnRequestType> {
        _neuralGraph = DefaultMutableAnonymousGraph(anonymizer)
        return this
    }

    fun addEnvironment(
        environment: Environment<ActivationType>,
        connectorsSpawner: Spawner<Activity<ActivationType?>, SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>,
    ): SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType, SpawnRequestType> {
        environment.allNodes.forEach { environmentNode ->
            val connectorNode = connectorsSpawner.spawn(environmentNode)
            _neuralGraph.add(connectorNode)
        }
        executors.add(EnvironmentExecutor(environment))
        return this
    }

    fun addFeedForwardExecutor(): SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType, SpawnRequestType> {
        val feedForwardExecutor = SimultaneousNeuralGraphExecutor(_neuralGraph)
        executors.add(feedForwardExecutor)
        return this
    }

    fun addBackPropagationExecutor(): SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType, SpawnRequestType> {
        val feedbackExecutor = SimultaneousCollaborativeGraphExecutor(_neuralGraph)
        executors.add(feedbackExecutor)
        return this
    }

    fun addSelfMorphingExecutor(
        connectionsAdvisor: ConnectionsAdvisor<ConnectionRequestType, SpawnRequestType, SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>,
        nodesSpawner: Spawner<SpawnRequestType, SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>,
    ): SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType, SpawnRequestType> {
        val selfMorphingExecutor = SmartNeuronSelfConnectionGraphExecutor(_neuralGraph, connectionsAdvisor, nodesSpawner)
        executors.add(selfMorphingExecutor)
        return this
    }

    fun build(): Executor {
        return ComplexExecutor(*executors.toTypedArray())
    }
}