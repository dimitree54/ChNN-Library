package we.rashchenko.chnn.node

interface SelfConnectable {
    val isExtraInputRequested: Boolean
    val inputsRemoveRequested: List<Int>
}