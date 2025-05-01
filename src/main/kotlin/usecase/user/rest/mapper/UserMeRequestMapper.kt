package ord.pumped.usecase.user.rest.mapper

import ord.pumped.common.mapping.IRouteMapper
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.rest.response.UserMeResponse

class UserMeRequestMapper : IRouteMapper<String, UserMeResponse, User> {

    //doesnt Happen
    override fun toDomain(string: String): User {
        TODO()
    }

    override fun toResponse(domain: User): UserMeResponse {
        return UserMeResponse(
            id = domain.id.toString(),
            username = domain.username,
            email = domain.email,
            createdAt = domain.createdAt,
            description = domain.description!!,
            profilePicture = domain.profilePicture!!,
            updatedAt = domain.updatedAt
        )
    }
}