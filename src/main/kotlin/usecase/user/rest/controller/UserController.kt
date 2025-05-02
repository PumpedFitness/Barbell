package ord.pumped.usecase.user.rest.controller

import io.ktor.server.application.*
import ord.pumped.common.security.service.ISecurityService
import ord.pumped.usecase.user.domain.service.IUserService
import ord.pumped.usecase.user.rest.mapper.UserLoginRequestMapper
import ord.pumped.usecase.user.rest.mapper.UserMeRequestMapper
import ord.pumped.usecase.user.rest.mapper.UserRegisterRequestMapper
import ord.pumped.usecase.user.rest.request.UserDeleteUserRequest
import ord.pumped.usecase.user.rest.request.UserLoginRequest
import ord.pumped.usecase.user.rest.request.UserRegisterRequest
import ord.pumped.usecase.user.rest.request.UserUpdatePasswordRequest
import ord.pumped.usecase.user.rest.response.UserLoginResponse
import ord.pumped.usecase.user.rest.response.UserMeResponse
import ord.pumped.usecase.user.rest.response.UserRegisterResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

object UserController : KoinComponent {
    val userRegisterRequestMapper: UserRegisterRequestMapper by inject()
    val userLoginRequestMapper: UserLoginRequestMapper by inject()
    val userMeRequestMapper: UserMeRequestMapper by inject()
    val securityService: ISecurityService by inject()
    val userService: IUserService by inject()

    fun registerUser(receiveAPIRequest: UserRegisterRequest): UserRegisterResponse {
        val userDomainObject = userRegisterRequestMapper.toDomain(receiveAPIRequest)
        val registeredUser = userService.registerUser(userDomainObject)
        return userRegisterRequestMapper.toResponse(registeredUser)
    }

    fun loginUser(request: UserLoginRequest, application: Application): UserLoginResponse {
        val loggedInUser = userService.loginUser(request.email, request.password)
        val token = securityService.createJWTToken(application, loggedInUser.id!!)
        return userLoginRequestMapper.toResponse(loggedInUser).copy(
            token = token.jwt
        )
    }

    fun updatePassword(userID: UUID, request: UserUpdatePasswordRequest) {
        userService.changePassword(userID, request.oldPassword, request.newPassword)
    }

    fun deleteUser(userID: UUID, request: UserDeleteUserRequest) {
        userService.deleteUser(userID, request.password)
    }

    fun getMe(userId: UUID): UserMeResponse {
       val user = userService.getUser(userId)
        return userMeRequestMapper.toResponse(user)
    }
}