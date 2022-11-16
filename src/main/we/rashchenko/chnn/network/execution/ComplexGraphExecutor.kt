package we.rashchenko.chnn.network.execution

class ComplexGraphExecutor(
    vararg executors: GraphExecutor,
) : GraphExecutor {
    private val executors = executors.toList()
    override fun tick() {
        executors.forEach { it.tick() }
    }
}