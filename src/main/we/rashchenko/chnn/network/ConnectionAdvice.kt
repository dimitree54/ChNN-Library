package we.rashchenko.chnn.network

data class ConnectionAdvice<ConnectionRequestType, NodeType>(
    val spawn: Boolean,
    val spawnRequest: ConnectionRequestType?,
    val addInputFromExistingNode: Boolean,
    val addInputFrom: NodeType?,
)