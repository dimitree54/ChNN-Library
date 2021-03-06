package we.rashchenko.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import we.rashchenko.environments.SimpleEnvironment
import we.rashchenko.networks.StochasticNeuralNetwork
import we.rashchenko.networks.builders.NeuralNetworkIn2DBuilderFixed
import we.rashchenko.neurons.zoo.RandomNeuronSampler

internal class MetricsCalculatorTest {

    @Test
    fun getAccuracy() {
        val nn = StochasticNeuralNetwork()
        val sampler = RandomNeuronSampler()
        val env = SimpleEnvironment(5)
        val builder = NeuralNetworkIn2DBuilderFixed(nn, sampler, 5)
        val id = builder.addInputOutputEnvironment(env)
        builder.addNeurons(env.activities.size)
        val calculator = MetricsCalculator(id, builder)
        assertThrows<IllegalArgumentException> {
            MetricsCalculator(id + 1, builder)
        }
        assertEquals(1.0, calculator.accuracy)
        env.outputActivities.first().active = true
        assertEquals(0.0, calculator.accuracy)
    }
}