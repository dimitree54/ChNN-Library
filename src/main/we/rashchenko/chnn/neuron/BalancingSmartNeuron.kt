package we.rashchenko.chnn.neuron

interface BalancingSmartNeuron<ActivationType, FeedbackType, ConnectionRequestType>:
    SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType> {
    val excitingInputIDs: Set<Int>
    val inhibitingInputIDs: Set<Int>
}