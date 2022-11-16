package we.rashchenko.chnn.network.execution

import we.rashchenko.chnn.node.CollaborativeUnit
import we.rashchenko.utility.graph.AnonymousGraph

class SimultaneousCollaborativeGraphExecutor<FeedbackType>(
    neuralGraph: AnonymousGraph<CollaborativeUnit<FeedbackType>>,
) : CollaborativeGraphExecutor<FeedbackType>(neuralGraph) {
    private fun gatherAllFeedbacks() = neuralGraph.allIds.associateWith { gatherFeedbacks(it) }
    override fun tick() = gatherAllFeedbacks().forEach { (id, feedbacks) ->
        reportFeedbacks(id, feedbacks)
    }
}