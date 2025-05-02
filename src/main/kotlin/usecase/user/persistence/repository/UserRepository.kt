package ord.pumped.usecase.user.persistence.repository

import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.persistence.dto.UserDTO
import ord.pumped.usecase.user.persistence.dto.UsersTable
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserRepository : IUserRepository {
    override fun save(user: User): UserDTO {
        return transaction {
            UserDTO.new {
                this.username = user.username
                this.password = user.password
                this.email = user.email
                this.updatedAt = user.updatedAt
                this.description = user.description!!
                this.profilePicture = user.profilePicture!!
            }
        }
    }

    override fun update(user: User): UserDTO {
        return transaction {
            UserDTO.findByIdAndUpdate(user.id!!) {
                it.username = user.username
                it.password = user.password
                it.email = user.email
                it.updatedAt = user.updatedAt
                it.description = user.description!!
                it.profilePicture = user.profilePicture!!
            }!!
        }
    }

    override fun delete(id: UUID) {
        transaction {
            UserDTO.findById(id)?.delete()
        }
    }

    override fun findByEmail(email: String): UserDTO? {
        return transaction {
            UserDTO.find {
                UsersTable.email eq email
            }.firstOrNull()
        }
    }

    override fun findByID(userID: UUID): UserDTO? {
        return transaction {
            UserDTO.find {
                UsersTable.id eq userID
            }.firstOrNull()
        }
    }
}