package ord.pumped.io.websocket.routing.messaging

import kotlinx.serialization.json.JsonElement

interface IWebsocketNotification {
    val message: String
    val status: Boolean

    fun asJson(): JsonElement
}