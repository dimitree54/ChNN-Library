package we.rashchenko.chnn.environment

interface Environment<ActivationType>{
    val size: Int
    fun getState(): List<ActivationType?>
    fun tick()
}