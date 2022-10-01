package we.rashchenko.chnn.loss

import we.rashchenko.chnn.network.Network

abstract class LossCalculator<ActivationType, FeedbackType>(private val network: Network<ActivationType, FeedbackType>) {
    abstract fun feedback2loss(feedback: FeedbackType): Double
    fun calculateLoss(): Double{
        return network.getAllNodes().flatMap { it.getFeedbacks().values }.sumOf { feedback2loss(it) }
    }
}