package app.pumped.domain.user

import app.pumped.api.requests.auth.LoginRequest
import app.pumped.domain.Repository
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class UserRepository: Repository<UUID, User> {
    override fun getAll(): List<User> {
        return transaction { User.all().toList() }
    }

    override fun get(key: UUID): User? {
        return transaction { User.findById(key) }
    }

    fun getByEmail(email: String): User? {
        return transaction { User.find { Users.email eq email }.firstOrNull() }
    }

    override fun insert(entity: User) {
        transaction {
            User.new {
            }
        }
    }

    override fun delete(entity: User) {
        transaction { entity.delete() }
    }

    override fun deleteById(key: UUID) {
        transaction { get(key)?.delete() }
    }

    override fun update(entity: User) {
        transaction { entity }
    }
}