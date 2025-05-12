package ord.pumped.io.websocket.routing.messaging

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import ord.pumped.io.defaultJson

@Serializable
data class UserProfileNotification(val email: String,
                                   override val message: String = "profile",
                                   override val status: Boolean = true
): IWebsocketNotification {

    override fun asJson(): JsonElement {
        return defaultJson.encodeToJsonElement(serializer(), this)
    }

}