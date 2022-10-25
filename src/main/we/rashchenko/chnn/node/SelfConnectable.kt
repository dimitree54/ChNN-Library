package we.rashchenko.chnn.node

interface SelfConnectable<ConnectionRequestType> {
    val extraInputRequest: ConnectionRequestType?
    val inputsRemoveRequest: List<Int>
}