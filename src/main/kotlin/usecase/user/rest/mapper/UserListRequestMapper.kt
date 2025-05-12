package ord.pumped.usecase.user.rest.mapper

import ord.pumped.common.mapping.IRouteMapper
import ord.pumped.usecase.user.domain.model.OnlineUser
import ord.pumped.usecase.user.rest.response.UserListResponse

class UserListRequestMapper: IRouteMapper<Any, UserListResponse, List<OnlineUser>> {
    override fun toDomain(request: Any): List<OnlineUser> {
        return listOf()
    }

    override fun toResponse(model: List<OnlineUser>): UserListResponse {
        return UserListResponse(model)
    }
}