package app.pumped.data.entities.socialstatistic

import app.pumped.domain.user.Users
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object Socialstatistics : UUIDTable() {

}

@Model //kser example
class Socialstatistic(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, Socialstatistic>(Socialstatistics)

}

object Likes : UUIDTable() {
    val user = reference("user", Users.id)
    val socialstatistic = reference("socialstatistic", Socialstatistics.id)
}

@Model //kser example
class Like(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, Like>(Likes)

}