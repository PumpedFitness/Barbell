package ord.pumped.usecase.user.rest.request.actions

import kotlinx.serialization.Serializable
import ord.pumped.io.websocket.routing.messaging.IWebsocketAction

@Serializable
data class NotifyUserAction(val userID: String): IWebsocketAction
