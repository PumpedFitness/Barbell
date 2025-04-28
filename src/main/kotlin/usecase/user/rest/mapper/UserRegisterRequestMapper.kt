package ord.pumped.usecase.user.rest.mapper

import ord.pumped.common.mapping.IRouteMapper
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.rest.request.UserRegisterRequest
import ord.pumped.usecase.user.rest.response.UserRegisterResponse

class UserRegisterRequestMapper : IRouteMapper<UserRegisterRequest, UserRegisterResponse, User> {
    override fun toDomain(userRequest: UserRegisterRequest): User {
        return User(
            id = null,
            username = userRequest.username,
            password = userRequest.password,
            email = userRequest.email,
        )
    }

    override fun toResponse(domain: User): UserRegisterResponse {
        return UserRegisterResponse(
                username = domain.username,
                email = domain.email,
                createdAt = domain.createdAt,
                updatedAt = domain.updatedAt
            )
    }
}