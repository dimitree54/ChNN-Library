package we.rashchenko.chnn.node

interface FeedbackProvider<FeedbackType> {
    fun getFeedbacks(): Map<Int, FeedbackType>
}