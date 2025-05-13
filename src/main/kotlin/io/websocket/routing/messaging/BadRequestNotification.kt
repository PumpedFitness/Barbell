package ord.pumped.io.websocket.routing.messaging

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import ord.pumped.io.defaultJson

@Serializable
data class BadRequestNotification(
    override val message: String = "Bad Request",
    override val status: Boolean = false,
    override val shouldNotify: Boolean = false
): IWebsocketResponse {

    override fun asJson(): JsonElement {
        return defaultJson.encodeToJsonElement(serializer(), this)
    }

}