package we.rashchenko.aang.loss

import we.rashchenko.chnn.loss.LossCalculator
import we.rashchenko.chnn.network.Network

class SimpleLossCalculator(network: Network<Boolean, Boolean>) : LossCalculator<Boolean, Boolean>(network) {
    override fun feedback2loss(feedback: Boolean): Double {
        return if (feedback) 0.0 else 1.0
    }
}