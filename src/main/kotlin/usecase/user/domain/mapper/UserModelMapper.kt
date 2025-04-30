package ord.pumped.usecase.user.domain.mapper

import ord.pumped.common.mapping.IModelMapper
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.persistence.dto.UserDTO
import org.jetbrains.exposed.sql.transactions.transaction

class UserModelMapper : IModelMapper<User, UserDTO> {
    override fun toDomain(user: UserDTO): User {
        return transaction {
            User(
                user.id.value,
                user.username,
                user.password,
                user.email,
                description = user.description,
                profilePicture = user.profilePicture,
                user.createdAt
            )
        }
    }
}