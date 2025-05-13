package ord.pumped.common.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import ord.pumped.io.defaultJson
import ord.pumped.io.websocket.routing.messaging.IWebsocketResponse

@Serializable
data class EmptyResponse(override val message: String = "",
                         override val shouldNotify: Boolean = false,
                         override val status: Boolean = true
): IWebsocketResponse {
    override fun asJson(): JsonElement {
        return defaultJson.encodeToJsonElement(serializer(), this)
    }
}
