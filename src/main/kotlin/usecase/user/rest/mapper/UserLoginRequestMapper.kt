package ord.pumped.usecase.user.rest.mapper

import ord.pumped.common.mapping.IRouteMapper
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.rest.request.UserLoginRequest
import ord.pumped.usecase.user.rest.response.UserLoginResponse

class UserLoginRequestMapper : IRouteMapper<UserLoginRequest, UserLoginResponse, User> {
    override fun toDomain(request: UserLoginRequest): User {
        return User(
            id = null,
            username = "null",
            email = request.email,
            password = request.password
        )
    }

    override fun toResponse(domain: User): UserLoginResponse {
        return UserLoginResponse(
            username = domain.username,
            email = domain.email,
            token = null,
        )
    }
}