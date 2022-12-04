package we.rashchenko.chnn.neuron

interface Activity<out ActivationType> {
    val activity: ActivationType
}