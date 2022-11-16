package we.rashchenko.chnn.node

interface CollaborativeUnit<FeedbackType> {
    val feedbacks: Map<Int, FeedbackType>
    fun listenForFeedbacks(feedbacks: Collection<FeedbackType>)
}