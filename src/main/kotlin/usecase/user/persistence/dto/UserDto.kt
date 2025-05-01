package ord.pumped.usecase.user.persistence.dto

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

object UsersTable : UUIDTable("barbell_users") {

    val username = varchar("username", 32)
    val password = varchar("password", 255)
    val email = varchar("email", 32)
    val description = varchar("description", 500)
    val profilePicture = varchar("profile_picture", 255)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
}


class UserDTO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, UserDTO>(UsersTable)

    var username by UsersTable.username
    var password by UsersTable.password
    var email by UsersTable.email
    var description by UsersTable.description
    var profilePicture by UsersTable.profilePicture
    val createdAt by UsersTable.createdAt
    var updatedAt by UsersTable.updatedAt
}

