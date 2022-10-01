package we.rashchenko.chnn.execution

import org.jgrapht.Graphs
import org.jgrapht.Graphs.successorListOf
import we.rashchenko.chnn.network.Network
import we.rashchenko.chnn.node.Node
import we.rashchenko.utils.UniqueQueue
import java.util.concurrent.atomic.AtomicBoolean

class QueueParallelLauncher<ActivationType, FeedbackType>(
    network: Network<ActivationType, FeedbackType>,
    private val numParallel: Int
) : NetworkLauncher<ActivationType, FeedbackType>(network) {
    private val queue = UniqueQueue<Node<ActivationType, FeedbackType>>()
    private fun addNodesToQueue(nodes: Collection<Node<ActivationType, FeedbackType>>) {
        synchronized(queue) {
            queue.addAll(nodes)
        }
    }

    private fun getNodeFromQueue(): Node<ActivationType, FeedbackType>? {
        synchronized(queue) {
            return queue.poll()
        }
    }

    init {
        addNodesToQueue(network.vertexSet())
    }

    val shutdownRequested = AtomicBoolean(false)
    private fun runThread() {
        while (!shutdownRequested.get()) {
            val node = getNodeFromQueue()
            if (node == null) {
                Thread.sleep(1000)
                continue
            }
            synchronized(node) { touch(node) }
            addNodesToQueue(successorListOf(network, node))
        }
    }

    override fun launch() {
        shutdownRequested.set(false)
        repeat(numParallel) {
            Thread(::runThread).start()
        }
    }

    override fun gatherInputs(node: Node<ActivationType, FeedbackType>): Map<Int, ActivationType> {
        return Graphs.predecessorListOf(network, node)
            .associateBy({ network.nodeIds[it]!! }, { synchronized(it) { it.getActivation() } })
    }

    override fun gatherFeedbacks(node: Node<ActivationType, FeedbackType>): List<FeedbackType> {
        return successorListOf(
            network,
            node
        ).mapNotNull { synchronized(it) { it.getFeedbacks() }[network.nodeIds[node]!!] }
    }
}