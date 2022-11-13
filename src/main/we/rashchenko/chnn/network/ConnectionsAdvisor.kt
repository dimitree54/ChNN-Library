package we.rashchenko.chnn.network

interface ConnectionsAdvisor<ConnectionRequestType, NodeType> {
    fun requestExtraInput(
        connectionRequest: ConnectionRequestType,
        requestingNode: NodeType,
    ): ConnectionAdvice<ConnectionRequestType, NodeType>

    fun requestRemoveInput(
        requestingNode: NodeType,
        connectedNode: NodeType,
    ): Boolean
}

