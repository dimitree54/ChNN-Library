package we.rashchenko.chnn.node

interface Activity<ActivationType> {
    val activity: ActivationType
}

interface Neuron<ActivationType>: Activity<ActivationType> {
    fun touch(inputs: Map<Int, ActivationType>)
    fun forgetInput(inputId: Int)
}

interface CollaborativeUnit<FeedbackType> {
    val feedbacks: Map<Int, FeedbackType>
    fun listenForFeedbacks(feedbacks: Collection<FeedbackType>)
}

interface SelfConnectingUnit<ConnectionRequestType> {
    val extraInputRequest: ConnectionRequestType?
    val inputsRemoveRequest: List<Int>
}

// todo consider renaming of SmartNeuron
interface SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType> :
    Neuron<ActivationType>, CollaborativeUnit<FeedbackType>, SelfConnectingUnit<ConnectionRequestType>
