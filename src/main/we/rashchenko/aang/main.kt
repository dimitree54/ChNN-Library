package we.rashchenko.aang

import we.rashchenko.aang.environment.PingPong
import we.rashchenko.aang.environment.SimpleExternallyControlledNode
import we.rashchenko.aang.loss.SimpleLossCalculator
import we.rashchenko.aang.network.SimpleConnectionsManagerBuilder
import we.rashchenko.aang.node.SimpleNodeSpawner
import we.rashchenko.chnn.execution.SimultaneousLauncher
import we.rashchenko.chnn.network.SelfMorphingNetwork

fun main() {
    val nodeSpawner = SimpleNodeSpawner()
    val environment = PingPong(3, 1)
    val environmentControlledNodes = environment.getState().map { SimpleExternallyControlledNode(nodeSpawner.spawn()) }
    val network = SelfMorphingNetwork(
        SimpleConnectionsManagerBuilder(nodeSpawner)
    ).also { network ->
        environmentControlledNodes.forEach { network.addNode(it) }
    }
    val lossCalculator = SimpleLossCalculator(network)
    val launcher = SimultaneousLauncher(network)

    while (true) {
        environment.tick()
        environmentControlledNodes.forEachIndexed { index, node ->
            node.externalActivity = environment.getState()[index]
        }
        while (lossCalculator.calculateLoss() > 0.0) {
            launcher.tick()
        }
    }
}