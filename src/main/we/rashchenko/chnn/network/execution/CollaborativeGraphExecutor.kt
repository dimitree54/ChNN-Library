package we.rashchenko.chnn.network.execution

import we.rashchenko.chnn.node.CollaborativeUnit
import we.rashchenko.utility.graph.AnonymousGraph

abstract class CollaborativeGraphExecutor<FeedbackType>(
    protected val neuralGraph: AnonymousGraph<CollaborativeUnit<FeedbackType>>,
) : Executor {
    protected fun gatherFeedbacks(id: Int) =
        neuralGraph.getListenersOf(id).mapNotNull { neuralGraph.deAnonymize(it).feedbacks[id] }

    protected fun reportFeedbacks(id: Int, feedbacks: Collection<FeedbackType>) =
        neuralGraph.deAnonymize(id).listenForFeedbacks(feedbacks)
}