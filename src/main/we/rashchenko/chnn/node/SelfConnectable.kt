package we.rashchenko.chnn.node

interface SelfConnectable {
    fun isExtraInputRequested(): Boolean
    fun inputsRemoveRequested(): List<Int>
}