package we.rashchenko.chnn.node

interface SelfConnectingUnit<ConnectionRequestType> {
    val extraInputRequest: ConnectionRequestType?
    val inputsRemoveRequest: List<Int>
}