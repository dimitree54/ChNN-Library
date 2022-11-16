package we.rashchenko.chnn.network

interface ConnectionsAdvisor<ConnectionRequestType, SpawnRequestType, NodeType> {
    fun requestExtraInput(
        connectionRequest: ConnectionRequestType,
        requestingNode: NodeType,
    ): ConnectionAdvice<ConnectionRequestType, SpawnRequestType, NodeType>

    fun requestRemoveInput(
        requestingNode: NodeType,
        connectedNode: NodeType,
    ): Boolean
}

