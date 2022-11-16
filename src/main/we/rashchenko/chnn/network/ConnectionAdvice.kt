package we.rashchenko.chnn.network

data class ConnectionAdvice<ConnectionRequestType, SpawnRequestType, NodeType>(
    val newNodeRequests: Pair<ConnectionRequestType, SpawnRequestType>?,
    val addInputFrom: NodeType?,
)