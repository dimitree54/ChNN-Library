package we.rashchenko.chnn.neuron

interface Neuron<ActivationType> : Activity<ActivationType> {
    fun touch(inputs: Map<Int, ActivationType>)
    fun forgetInput(inputId: Int)
}