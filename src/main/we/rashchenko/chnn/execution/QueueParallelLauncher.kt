package we.rashchenko.chnn.execution

import we.rashchenko.chnn.network.Network
import we.rashchenko.utils.UniqueQueue
import java.util.concurrent.atomic.AtomicBoolean

class QueueParallelLauncher<ActivationType, FeedbackType>(
    private val network: Network<ActivationType, FeedbackType>,
    private val numParallel: Int
): NetworkLauncher {
    private val queue = UniqueQueue<Int>()
    private fun addNodesToQueue(ids: Collection<Int>) {
        synchronized(queue) {
            ids.forEach { locks.putIfAbsent(it, Object()) }
            queue.addAll(ids)
        }
    }

    private fun getNodeFromQueue(): Int? {
        synchronized(queue) {
            return queue.poll()
        }
    }

    init {
        addNodesToQueue(network.getAllIds())
    }

    private val locks = mutableMapOf<Int, Any>()

    private val shutdownRequested = AtomicBoolean(false)
    override fun stop() {
        shutdownRequested.set(true)
    }
    private fun runThread() {
        while (!shutdownRequested.get()) {
            val nodeId = getNodeFromQueue()
            if (nodeId == null) {
                Thread.sleep(1000)
                continue
            }
            synchronized(locks[nodeId]!!) {
                val feedbacks = gatherFeedbacks(nodeId)
                val inputs = gatherInputs(nodeId)
                network.touch(nodeId, feedbacks, inputs)
            }
            addNodesToQueue(network.getNodesListeningNode(nodeId))
        }
    }

    private fun gatherInputs(nodeId: Int): Map<Int, ActivationType> {
        synchronized(locks[nodeId]!!) {
            return network.gatherInputs(nodeId)
        }
    }

    private fun gatherFeedbacks(nodeId: Int): List<FeedbackType> {
        synchronized(locks[nodeId]!!) {
            return network.gatherFeedbacks(nodeId)
        }
    }

    override fun launch() {
        shutdownRequested.set(false)
        repeat(numParallel) {
            Thread(::runThread).start()
        }
    }
}