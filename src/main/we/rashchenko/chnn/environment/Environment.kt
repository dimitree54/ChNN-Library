package we.rashchenko.chnn.environment

import we.rashchenko.chnn.network.ConnectionsAdvisor
import we.rashchenko.chnn.network.execution.*
import we.rashchenko.chnn.node.Activity
import we.rashchenko.chnn.node.SmartNeuron
import we.rashchenko.utility.Spawner
import we.rashchenko.utility.Ticking
import we.rashchenko.utility.graph.DefaultDirectedGraph
import we.rashchenko.utility.graph.DirectedGraph
import we.rashchenko.utility.graph.NodesCollection

// todo create network with environment
interface Environment<ActivationType>: NodesCollection<Activity<ActivationType?>>

class NeuralNetwork<ActivationType, FeedbackType, ConnectionRequestType>(
    private val environment: Environment<ActivationType>,
    private val spawner: Spawner<ConnectionRequestType, SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>,
    private val connectorsSpawner: Spawner<Activity<ActivationType?>, SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>,
    private val connectionsAdvisor: ConnectionsAdvisor<ConnectionRequestType, SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>
){
    private val neuralGraph = DefaultDirectedGraph<SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>().also{
        environment.allNodes.forEach { environmentNode ->
            val connectorNode = connectorsSpawner.spawn(environmentNode)
            it.add(connectorNode)
        }
    }
    private val anonymizer = SequentialAnonymizer<SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>()
    private val executor = ComplexGraphExecutor(
        SimultaneousNeuralGraphExecutor(neuralGraph, anonymizer) as GraphExecutor<SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>,
        SimultaneousCollaborativeGraphExecutor(neuralGraph, anonymizer) as GraphExecutor<SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>,
        DefaultSelfConnectingGraphExecutor(neuralGraph, anonymizer, connectionsAdvisor, spawner) as GraphExecutor<SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>>
    )
}
