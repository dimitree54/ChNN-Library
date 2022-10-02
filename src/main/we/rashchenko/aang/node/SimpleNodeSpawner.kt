package we.rashchenko.aang.node

import we.rashchenko.chnn.node.Node
import we.rashchenko.chnn.node.NodeSpawner

class SimpleNodeSpawner: NodeSpawner<Boolean, Boolean> {
    override fun spawn(): Node<Boolean, Boolean> {
        return SimpleNode()
    }

    override fun spawn(node: Node<Boolean, Boolean>): Node<Boolean, Boolean> {
        return spawn()
    }
}