package we.rashchenko.chnn.execution

import we.rashchenko.chnn.environment.Environment
import we.rashchenko.chnn.network.Network
import we.rashchenko.chnn.node.Node

abstract class NetworkLauncher<ActivationType, FeedbackType>(
    protected val network: Network<ActivationType, FeedbackType>
) {
    protected val environments = mutableListOf<Environment<FeedbackType>>()
    fun addEnvironment(environment: Environment<FeedbackType>) {
        environments.add(environment)
    }
    abstract fun launch()

    abstract fun gatherInputs(node: Node<ActivationType, FeedbackType>): Map<Int, ActivationType>

    abstract fun gatherFeedbacks(node: Node<ActivationType, FeedbackType>): List<FeedbackType>
}