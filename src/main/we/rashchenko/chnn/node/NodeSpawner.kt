package we.rashchenko.chnn.node

interface NodeSpawner<ActivationType, FeedbackType> {
    fun spawn(): Node<ActivationType, FeedbackType>
}