package ord.pumped.common.request.actions

import kotlinx.serialization.Serializable
import ord.pumped.io.websocket.routing.messaging.IWebsocketAction

@Serializable
data class EmptyAction(val empty: String = ""): IWebsocketAction
