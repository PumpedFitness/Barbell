package ord.pumped.usecase.user.rest.controller

import io.ktor.server.application.*
import ord.pumped.common.security.service.ISecurityService
import ord.pumped.io.websocket.IWebsocketHandler
import ord.pumped.usecase.user.domain.mapper.OnlineUserModelMapper
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.domain.service.IUserService
import ord.pumped.usecase.user.rest.mapper.di.UserRequestMappers
import ord.pumped.usecase.user.rest.request.UserDeleteUserRequest
import ord.pumped.usecase.user.rest.request.UserLoginRequest
import ord.pumped.usecase.user.rest.request.UserRegisterRequest
import ord.pumped.usecase.user.rest.request.UserUpdatePasswordRequest
import ord.pumped.usecase.user.rest.response.UserListResponse
import ord.pumped.usecase.user.rest.response.UserLoginResponse
import ord.pumped.usecase.user.rest.response.UserMeResponse
import ord.pumped.usecase.user.rest.response.UserRegisterResponse
import ord.pumped.usecase.user.rest.response.notifications.UserNotification
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

object UserController : KoinComponent {
    val mappers: UserRequestMappers by inject()

    val onlineUserModelMapper: OnlineUserModelMapper by inject()
    val securityService: ISecurityService by inject()
    val userService: IUserService by inject()
    val websocketHandler: IWebsocketHandler by inject()

    fun registerUser(receiveAPIRequest: UserRegisterRequest): UserRegisterResponse {
        val userDomainObject = mappers.register.toDomain(receiveAPIRequest)
        val registeredUser = userService.registerUser(userDomainObject)
        return mappers.register.toResponse(registeredUser)
    }

    fun loginUser(request: UserLoginRequest, application: Application): UserLoginResponse {
        val loggedInUser = userService.loginUser(request.email, request.password)
        val token = securityService.createJWTToken(application, loggedInUser.id!!)
        return mappers.login.toResponse(loggedInUser).copy(
            token = token.jwt
        )
    }

    fun logoutUser(tokenID: UUID) {
        securityService.blacklistToken(tokenID)
    }

    fun updatePassword(userID: UUID, request: UserUpdatePasswordRequest) {
        userService.changePassword(userID, request.oldPassword, request.newPassword)
    }

    fun deleteUser(userID: UUID, request: UserDeleteUserRequest) {
        userService.deleteUser(userID, request.password)
    }

    fun getMe(userId: UUID): UserMeResponse {
        val user = userService.getUser(userId)
        return mappers.me.toResponse(user)
    }

    fun getOnlineUsers(): UserListResponse {
        val users = websocketHandler.getOnlineUsers()
        return mappers.list.toResponse(users.map { onlineUserModelMapper.toDomain(userService.getUser(it)) })
    }

    fun notifyUser(userID: String, notifier: User) {
        val user = userService.getUserOrNull(UUID.fromString(userID)) ?: return
        websocketHandler.sendNotificationToUser(user.id!!, UserNotification("You got a new message from ${notifier.username}!", "Hey ho!"))
    }
}