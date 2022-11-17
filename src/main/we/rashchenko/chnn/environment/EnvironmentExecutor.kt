package we.rashchenko.chnn.environment

import we.rashchenko.chnn.network.execution.Executor

class EnvironmentExecutor(private val environment: Environment<*>) : Executor {
    override fun tick() {
        environment.tick()
    }
}