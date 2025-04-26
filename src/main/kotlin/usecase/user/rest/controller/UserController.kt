package ord.pumped.usecase.user.rest.controller

import ord.pumped.usecase.user.domain.service.IUserService
import ord.pumped.usecase.user.rest.mapper.UserLoginRequestMapper
import ord.pumped.usecase.user.rest.mapper.UserMeRequestMapper
import ord.pumped.usecase.user.rest.mapper.UserRegisterRequestMapper
import ord.pumped.usecase.user.rest.request.UserLoginRequest
import ord.pumped.usecase.user.rest.request.UserRegisterRequest
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
    val userService: IUserService by inject()

    fun registerUser(receiveAPIRequest: UserRegisterRequest): UserRegisterResponse {
        val userDomainObject = userRegisterRequestMapper.toDomain(receiveAPIRequest)
        val registeredUser = userService.registerUser(userDomainObject)
        return userRegisterRequestMapper.toResponse(registeredUser)
    }

    fun loginUser(request: UserLoginRequest): UserLoginResponse {
        val loggedInUser = userService.loginUser(request.email, request.password)
        return userLoginRequestMapper.toResponse(loggedInUser)
    }

    fun getMe(userID: String?): UserMeResponse {
        val userID = UUID.fromString(userID)
        val user = userService.getUser(userID)
        return userMeRequestMapper.toResponse(user)
    }


}