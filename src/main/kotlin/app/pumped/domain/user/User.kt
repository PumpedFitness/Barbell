package app.pumped.domain.user

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.statix.Model
import java.util.UUID

object Users : UUIDTable() {
    val email = varchar("email", 64).uniqueIndex()
    val username = varchar("username", 32).uniqueIndex()
    val password = varchar("password", 255)
}

@Model //kser example
class User(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, User>(Users)

    var email by Users.email
    var username by Users.username
    var password by Users.password

}