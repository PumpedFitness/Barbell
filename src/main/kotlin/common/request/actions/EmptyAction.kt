package ord.pumped.common.request.actions

import ord.pumped.io.websocket.routing.messaging.IWebsocketAction

data class EmptyAction(val empty: String = ""): IWebsocketAction
