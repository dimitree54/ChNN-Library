package we.rashchenko.chnn.node

interface Node<ActivationType, FeedbackType>: Activity<ActivationType>, FeedbackProvider<FeedbackType>, SelfConnectable {
    fun touch(inputs: Map<Int, ActivationType>)
    fun update()
    fun reportFeedbacks(feedbacks: List<FeedbackType>)

    fun addInput(inputId: Int)
    fun removeInput(inputId: Int)
}
