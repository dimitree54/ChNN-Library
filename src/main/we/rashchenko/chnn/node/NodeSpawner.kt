package we.rashchenko.chnn.node

interface NodeSpawner<ActivationType, FeedbackType> {
    fun spawn(initialActivity: ActivationType): Node<ActivationType, FeedbackType>
}