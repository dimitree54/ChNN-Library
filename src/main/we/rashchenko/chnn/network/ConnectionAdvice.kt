package we.rashchenko.chnn.network

data class ConnectionAdvice<ConnectionRequestType, SpawnRequestType>(
    val newNodeRequests: Pair<ConnectionRequestType, SpawnRequestType>?,
    val addInputFrom: Int?,
)