package we.rashchenko.chnn.environment

import we.rashchenko.chnn.network.execution.GraphExecutor
import we.rashchenko.chnn.node.Activity
import we.rashchenko.utility.Ticking
import we.rashchenko.utility.graph.NodesCollection

interface Environment<ActivationType> : NodesCollection<Activity<ActivationType?>>, Ticking

// todo why environment executor is GraphExecutor, but Environment is not Graph?
class EnvironmentExecutor(private val environment: Environment<*>) : GraphExecutor{
    override fun tick() {
        environment.tick()
    }
}