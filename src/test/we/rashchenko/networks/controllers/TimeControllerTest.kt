package we.rashchenko.networks.controllers

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import we.rashchenko.base.Feedback
import we.rashchenko.neurons.ControlledNeuron
import we.rashchenko.neurons.SlowNeuron
import we.rashchenko.neurons.zoo.RandomNeuron

internal class TimeControllerTest {

    @Test
    fun getControllerFeedbacks() {
        val neurons = listOf(
            ControlledNeuron(SlowNeuron()).apply { control = true },
            ControlledNeuron(RandomNeuron(0.5f)).apply { control = true },
            ControlledNeuron(RandomNeuron(0.5f)).apply { control = true },
            ControlledNeuron(RandomNeuron(0.5f)).apply { control = true },
            ControlledNeuron(SlowNeuron()).apply { control = true },
        )

        repeat(100) { timeStep ->
            neurons.forEach {
                it.touch(timeStep, timeStep.toLong())
                it.update(Feedback.NEUTRAL, timeStep.toLong())
                it.getFeedback(timeStep)
                it.forgetSource(timeStep)
                it.active
            }
        }

        val controllerFeedbacks = TimeController().getControllerFeedbacks(neurons)
        assertTrue(controllerFeedbacks[0] < Feedback.NEUTRAL)
        assertTrue(controllerFeedbacks[1] > Feedback.NEUTRAL)
        assertTrue(controllerFeedbacks[2] > Feedback.NEUTRAL)
        assertTrue(controllerFeedbacks[3] > Feedback.NEUTRAL)
        assertTrue(controllerFeedbacks[4] < Feedback.NEUTRAL)
    }
}
