package ord.pumped.usecase.user.rest.controller

import ord.pumped.usecase.user.domain.service.IUserService
import ord.pumped.usecase.user.rest.mapper.UserRequestMapper
import ord.pumped.usecase.user.rest.request.UserRegisterRequest
import ord.pumped.usecase.user.rest.response.UserRegisterResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object UserController : KoinComponent {
    val userRequestMapper: UserRequestMapper by inject()
    val userService: IUserService by inject()

    fun registerUser(receiveAPIRequest: UserRegisterRequest): UserRegisterResponse {
        val userDomainObject = userRequestMapper.toDomain(receiveAPIRequest)
        val registeredUser = userService.registerUser(userDomainObject)
        return userRequestMapper.toResponse(registeredUser)
    }


}