package ord.pumped.io.websocket.routing.messaging

import kotlinx.serialization.Serializable

@Serializable
data class BadRequestNotification(
    override val message: String = "Bad Request",
    override val status: Boolean = false
): IWebsocketNotification