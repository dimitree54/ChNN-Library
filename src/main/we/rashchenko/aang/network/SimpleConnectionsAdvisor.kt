package we.rashchenko.aang.network

import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import we.rashchenko.chnn.network.ConnectionsAdvisor
import we.rashchenko.chnn.network.ConnectionsAdvisorBuilder
import we.rashchenko.chnn.node.Node
import we.rashchenko.chnn.node.NodeSpawner

class SimpleConnectionsAdvisor(
    private val nodesSpawner: NodeSpawner<Boolean, Boolean>,
    private val graph: DefaultDirectedGraph<Node<Boolean, Boolean>, DefaultEdge>
) :
    ConnectionsAdvisor<Boolean, Boolean> {
    override fun requestExtraConnection(node: Node<Boolean, Boolean>): List<Node<Boolean, Boolean>> {
        val alreadyConnectedNodes = graph.incomingEdgesOf(node).map { graph.getEdgeSource(it) }
        val connectionCandidates = graph.vertexSet().filter { it !in alreadyConnectedNodes && it.getActivation() }

        val nodeToConnect = if (connectionCandidates.isEmpty()) {
            nodesSpawner.spawn()
        }
        else{
            connectionCandidates.random()
        }
        return listOf(nodeToConnect)
    }

    override fun requestDropConnection(requestingNode: Node<Boolean, Boolean>, connectedNode: Node<Boolean, Boolean>): Boolean {
        return graph.getEdge(connectedNode, requestingNode) != null
    }
}

class SimpleConnectionsManagerBuilder(private val nodeSpawner: NodeSpawner<Boolean, Boolean>) :
    ConnectionsAdvisorBuilder<Boolean, Boolean> {
    override fun build(
        graph: DefaultDirectedGraph<Node<Boolean, Boolean>, DefaultEdge>
    ): ConnectionsAdvisor<Boolean, Boolean> {
        return SimpleConnectionsAdvisor(nodeSpawner, graph)
    }
}