package we.rashchenko.chnn.node

interface FeedbackProvider<FeedbackType> {
    val feedbacks: Map<Int, FeedbackType>
}
