package we.rashchenko.utility

interface Spawner<RequestType, SpawnType> {
    fun spawn(request: RequestType): SpawnType
}