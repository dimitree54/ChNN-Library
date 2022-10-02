package we.rashchenko.chnn.loss

import we.rashchenko.chnn.network.Network

abstract class LossCalculator<ActivationType, FeedbackType>(private val network: Network<ActivationType, FeedbackType>) {
    abstract fun feedback2loss(feedback: FeedbackType): Double
    fun calculateLoss(): Double{
        return network.getAllIds().flatMap { network.getFeedbacks(it).values }.sumOf { feedback2loss(it) }
    }
}