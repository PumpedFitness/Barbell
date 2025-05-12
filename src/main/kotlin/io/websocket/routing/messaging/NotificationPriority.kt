package ord.pumped.io.websocket.routing.messaging

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class NotificationPriority {
    @SerialName("HIGH") HIGH,
    @SerialName("MEDIUM") MEDIUM,
    @SerialName("LOW") LOW
}