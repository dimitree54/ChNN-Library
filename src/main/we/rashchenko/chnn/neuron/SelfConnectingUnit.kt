package we.rashchenko.chnn.neuron

interface SelfConnectingUnit<ConnectionRequestType> {
    val extraInputRequest: ConnectionRequestType?
    val inputsRemoveRequest: List<Int>
}