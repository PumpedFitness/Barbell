package ord.pumped.io.websocket.auth.controller

import ord.pumped.usecase.user.domain.model.User
import java.util.UUID

fun interface IWebsocketUpgradeController {
    fun upgradeWebsocket(socketID: UUID, user: User)
}