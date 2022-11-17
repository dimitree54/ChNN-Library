package we.rashchenko.chnn.network.execution

class ComplexExecutor(
    vararg executors: Executor,
) : Executor {
    private val executors = executors.toList()
    override fun tick() {
        executors.forEach { it.tick() }
    }
}