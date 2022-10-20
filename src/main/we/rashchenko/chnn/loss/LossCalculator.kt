package we.rashchenko.chnn.loss

import we.rashchenko.chnn.network.Network

abstract class LossCalculator<ActivationType, FeedbackType>(private val network: Network<ActivationType, FeedbackType>) {
    abstract fun calculateLoss(): Double
}