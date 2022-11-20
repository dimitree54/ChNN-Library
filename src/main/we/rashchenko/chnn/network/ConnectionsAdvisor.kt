package we.rashchenko.chnn.network

interface ConnectionsAdvisor<ConnectionRequestType, SpawnRequestType> {
    fun requestExtraInput(
        connectionRequest: ConnectionRequestType,
        requestingId: Int,
    ): ConnectionAdvice<ConnectionRequestType, SpawnRequestType>

    fun requestRemoveInput(
        requestingId: Int,
        connectedId: Int,
    ): Boolean
}

