package we.rashchenko.chnn.node

interface Node<ActivationType, FeedbackType, ConnectionRequestType>: FeedbackProvider<FeedbackType>, SelfConnectable<ConnectionRequestType> {
    val generation: Int
    val activity: ActivationType
    fun touch(inputs: Map<Int, ActivationType>)
    fun update()
    fun reportFeedbacks(feedbacks: List<FeedbackType>)

    fun addInput(inputId: Int)
    fun removeInput(inputId: Int)
}
