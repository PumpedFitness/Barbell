package ord.pumped.usecase.user.rest.controller

import ord.pumped.usecase.user.domain.service.IUserService
import ord.pumped.usecase.user.rest.mapper.UserMeRequestMapper
import ord.pumped.usecase.user.rest.request.UserUpdateProfileRequest
import ord.pumped.usecase.user.rest.response.UserMeResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

object UserProfileController : KoinComponent {
    val userService: IUserService by inject()
    val userMeRequestMapper: UserMeRequestMapper by inject()

    fun postUserProfile(userID: UUID, receive: UserUpdateProfileRequest): UserMeResponse {
        val updatedUser = userService.updateUserProfile(userID, receive)
        return userMeRequestMapper.toResponse(updatedUser)
    }

}