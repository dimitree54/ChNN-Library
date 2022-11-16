package we.rashchenko.utility

interface Spawner<SpawnRequestType, SpawnType> {
    fun spawn(request: SpawnRequestType): SpawnType
}