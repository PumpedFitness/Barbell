package ord.pumped.io.websocket.auth

import ord.pumped.io.websocket.IWebsocketHandler
import ord.pumped.io.websocket.auth.controller.IWebsocketUpgradeController
import ord.pumped.io.websocket.auth.exception.UnknownSocketException
import ord.pumped.usecase.user.domain.model.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

class WebsocketUpgradeControllerAdapter: IWebsocketUpgradeController, KoinComponent {

    private val websocketHandler by inject<IWebsocketHandler>()

    override fun upgradeWebsocket(socketID: UUID, user: User) {
        if (!websocketHandler.hasSession(socketID)) {
            throw UnknownSocketException()
        }

        websocketHandler.associateUserWithSocketID(socketID, user)
    }
}