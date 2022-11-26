package we.rashchenko.utility

interface Spawner<in SpawnRequestType, out SpawnType> {
    fun spawn(request: SpawnRequestType): SpawnType
}