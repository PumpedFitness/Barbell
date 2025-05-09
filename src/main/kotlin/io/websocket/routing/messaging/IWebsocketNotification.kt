package ord.pumped.io.websocket.routing.messaging

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
@Polymorphic
sealed interface IWebsocketNotification {
    val message: String
    val status: Boolean
}