package ord.pumped.usecase.user.rest.mapper

import ord.pumped.common.mapping.IRouteMapper
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.rest.request.UserRegisterRequest
import ord.pumped.usecase.user.rest.response.UserRegisterResponse

class UserRequestMapper : IRouteMapper<UserRegisterRequest, UserRegisterResponse, User> {
    override fun toDomain(userRequest: UserRegisterRequest): User {
        return User(
            id = null,
            username = userRequest.username,
            password = userRequest.password,
            email = userRequest.email,
        )
    }

    override fun toResponse(dto: User): UserRegisterResponse {
        TODO("Not yet implemented")
    }
}