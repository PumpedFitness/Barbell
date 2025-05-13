package ord.pumped.usecase.user.rest.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import ord.pumped.io.defaultJson
import ord.pumped.io.websocket.routing.messaging.IWebsocketResponse
import ord.pumped.usecase.user.domain.model.OnlineUser

@Serializable
data class UserListResponse(
    val users: List<OnlineUser>,
    override val shouldNotify: Boolean = false,
    override val message: String = "",
    override val status: Boolean = true
): IWebsocketResponse {
    override fun asJson(): JsonElement = defaultJson.encodeToJsonElement(serializer(), this)
}