package we.rashchenko.chnn.neuron

interface CollaborativeUnit<FeedbackType> {
    val feedbacks: Map<Int, FeedbackType>
    fun listenForFeedbacks(feedbacks: Collection<FeedbackType>)
}