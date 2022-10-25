package we.rashchenko.chnn.node

interface NodeSpawner<ActivationType, FeedbackType, ConnectionRequestType> {
    fun spawn(initialActivity: ActivationType): Node<ActivationType, FeedbackType, ConnectionRequestType>
}