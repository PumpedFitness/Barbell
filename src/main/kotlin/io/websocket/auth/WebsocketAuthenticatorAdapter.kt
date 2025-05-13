package ord.pumped.io.websocket.auth

import io.ktor.server.application.*
import ord.pumped.common.exceptions.UnauthorizedException
import ord.pumped.configuration.userID
import ord.pumped.usecase.user.domain.mapper.UserModelMapper
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.persistence.repository.IUserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WebsocketAuthenticatorAdapter: IWebsocketAuthenticator, KoinComponent {

    private val userRepository by inject<IUserRepository>()
    private val userMapper by inject<UserModelMapper>()

    override fun authenticate(call: ApplicationCall): User? {
        val userID = try {
            call.userID()
        } catch (e: UnauthorizedException) {
            return null
        }

        val userDTO = userRepository.findByID(userID) ?: return null
        return userMapper.toDomain(userDTO)
    }
}