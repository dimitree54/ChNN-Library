package we.rashchenko.chnn.node

interface SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType> : Neuron<ActivationType>,
    CollaborativeUnit<FeedbackType>, SelfConnectingUnit<ConnectionRequestType>
