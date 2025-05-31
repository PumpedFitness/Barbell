package ord.pumped.io.websocket.auth

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import ord.pumped.io.defaultJson
import ord.pumped.io.websocket.routing.messaging.IWebsocketResponse

@Serializable
class UUIDResponse(val id: String,
                   override val shouldNotify: Boolean = true,
                   override val message: String = "UUID",
                   override val status: Boolean = true
): IWebsocketResponse {
    override fun asJson(): JsonElement {
        return defaultJson.encodeToJsonElement(serializer(), this)
    }
}