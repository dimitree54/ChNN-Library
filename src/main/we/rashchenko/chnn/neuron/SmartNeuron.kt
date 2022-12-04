package we.rashchenko.chnn.neuron

interface SmartNeuron<ActivationType, FeedbackType, ConnectionRequestType> : Neuron<ActivationType>,
    CollaborativeUnit<FeedbackType>, SelfConnectingUnit<ConnectionRequestType>
