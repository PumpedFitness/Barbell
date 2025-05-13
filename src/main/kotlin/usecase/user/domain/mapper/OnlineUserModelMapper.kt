package ord.pumped.usecase.user.domain.mapper

import ord.pumped.common.mapping.IModelMapper
import ord.pumped.usecase.user.domain.model.OnlineUser
import ord.pumped.usecase.user.domain.model.User

class OnlineUserModelMapper: IModelMapper<OnlineUser, User> {

    override fun toDomain(dto: User): OnlineUser {
        return OnlineUser(
            dto.id!!.toString(),
            dto.username,
            dto.description,
            dto.profilePicture
        )
    }
}