package we.rashchenko.chnn.node

interface Activity<ActivationType>{
    fun getActivation(): ActivationType
}