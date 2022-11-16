package we.rashchenko.chnn.node

// todo consider renaming of SmartNeuron
interface SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType> : Neuron<ActivationType>,
    CollaborativeUnit<FeedbackType>, SelfConnectingUnit<ConnectionRequestType>
