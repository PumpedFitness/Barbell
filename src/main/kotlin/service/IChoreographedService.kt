package ord.pumped.service

fun interface IChoreographedService {
    suspend fun sendEvent(event: IEvent)
}