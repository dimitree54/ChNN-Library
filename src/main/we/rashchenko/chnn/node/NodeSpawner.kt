package we.rashchenko.chnn.node

interface NodeSpawner<ActivationType, FeedbackType> {
    fun spawn(): Node<ActivationType, FeedbackType>
    fun spawn(node: Node<ActivationType, FeedbackType>): Node<ActivationType, FeedbackType>
}