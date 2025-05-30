package ord.pumped.io.websocket.auth

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import ord.pumped.io.defaultJson
import ord.pumped.io.websocket.routing.messaging.IWebsocketResponse

@Serializable
class UUIDResponse(val id: String): IWebsocketResponse {

    override val shouldNotify: Boolean
        get() = false
    override val message: String
        get() = "UUID"
    override val status: Boolean
        get() = true

    override fun asJson(): JsonElement {
        return defaultJson.encodeToJsonElement(serializer(), this)
    }
}