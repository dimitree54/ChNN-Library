package we.rashchenko.chnn.network.execution

import we.rashchenko.chnn.environment.Environment
import we.rashchenko.chnn.environment.EnvironmentExecutor
import we.rashchenko.chnn.network.ConnectionsAdvisor
import we.rashchenko.chnn.node.Activity
import we.rashchenko.chnn.node.SmartNeuron
import we.rashchenko.utility.Spawner
import we.rashchenko.utility.graph.DefaultMutableAnonymousGraph

class SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType>(
    private val neuralGraph: DefaultMutableAnonymousGraph<SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>,
) {
    private val executors = mutableListOf<Executor>()
    fun <EnvironmentActivationType> addEnvironment(
        environment: Environment<EnvironmentActivationType>,
        connectorsSpawner: Spawner<Activity<EnvironmentActivationType>, SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>,
    ): SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType> {
        environment.allNodes.forEach { environmentNode ->
            val connectorNode = connectorsSpawner.spawn(environmentNode)
            neuralGraph.add(connectorNode)
        }
        executors.add(EnvironmentExecutor(environment))
        return this
    }

    fun addFeedForwardExecutor(): SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType> {
        val feedForwardExecutor = SimultaneousNeuralGraphExecutor(neuralGraph)
        executors.add(feedForwardExecutor)
        return this
    }

    fun addBackPropagationExecutor(): SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType> {
        val feedbackExecutor = SimultaneousCollaborativeGraphExecutor(neuralGraph)
        executors.add(feedbackExecutor)
        return this
    }

    fun <SpawnRequestType> addSelfMorphingExecutor(
        connectionsAdvisor: ConnectionsAdvisor<ConnectionRequestType, SpawnRequestType>,
        nodesSpawner: Spawner<SpawnRequestType, SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>,
    ): SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType> {
        val selfMorphingExecutor = SmartNeuronSelfConnectionGraphExecutor(neuralGraph, connectionsAdvisor, nodesSpawner)
        executors.add(selfMorphingExecutor)
        return this
    }

    fun addExecutor(executor: Executor): SmartNetworkExecutorBuilder<ActivationType, FeedbackType, ConnectionRequestType> {
        executors.add(executor)
        return this
    }

    fun build(): Executor {
        return ComplexExecutor(*executors.toTypedArray())
    }
}