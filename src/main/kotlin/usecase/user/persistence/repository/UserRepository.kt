package ord.pumped.usecase.user.persistence.repository

import ord.pumped.common.IRepository
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.persistence.dto.UserDTO
import ord.pumped.usecase.user.persistence.dto.UsersTable
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository : IRepository<User, UserDTO> {
    override fun save(user: User): UserDTO {
        return transaction {
            UserDTO.new {
                this.username = user.username
                this.password = user.password
                this.email = user.email
                this.updatedAt = user.updatedAt
            }
        }
    }

    fun findByEmail(email: String): UserDTO? {
        return transaction {
            UserDTO.find {
                UsersTable.email eq email
            }.firstOrNull()
        }
    }
}