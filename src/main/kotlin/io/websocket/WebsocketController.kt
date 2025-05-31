package ord.pumped.io.websocket

import io.ktor.server.response.*
import ord.pumped.io.websocket.auth.controller.IWebsocketUpgradeService
import ord.pumped.io.websocket.auth.exception.UnknownSocketException
import ord.pumped.usecase.user.domain.mapper.UserModelMapper
import ord.pumped.usecase.user.persistence.repository.IUserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

object WebsocketController: KoinComponent {

    val userRepo by inject<IUserRepository>()
    val userMapper by inject<UserModelMapper>()
    val websocketUpgradeService by inject<IWebsocketUpgradeService>()
    val websocketHandler by inject<IWebsocketHandler>()

    fun upgradeWebsocket(socketID: UUID, userID: UUID) {
        val userDTO = userRepo.findByID(userID) ?: throw UnknownSocketException()
        val user = userMapper.toDomain(userDTO)

        websocketUpgradeService.upgradeWebsocket(socketID, user)
    }

    fun destroyWebsocket(userID: UUID) {
        websocketHandler.closeForUser(userID)
    }
}