package ord.pumped.usecase.user.rest.response.notifications

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import ord.pumped.io.defaultJson
import ord.pumped.io.websocket.routing.messaging.IWebsocketNotification
import ord.pumped.io.websocket.routing.messaging.NotificationPriority

@Serializable
data class UserNotification(
    override val body: String,
    override val title: String,
    override val priority: NotificationPriority = NotificationPriority.LOW,
    override val message: String = "",
    override val status: Boolean = true,
    override val shouldNotify: Boolean = true,
): IWebsocketNotification {
    override fun asJson(): JsonElement {
        return defaultJson.encodeToJsonElement(serializer(), this)
    }
}
