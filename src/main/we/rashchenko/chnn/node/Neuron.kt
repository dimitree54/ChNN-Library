package we.rashchenko.chnn.node

interface Neuron<ActivationType> : Activity<ActivationType> {
    fun touch(inputs: Map<Int, ActivationType>)
    fun forgetInput(inputId: Int)
}