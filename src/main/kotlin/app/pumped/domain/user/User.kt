package app.pumped.domain.user

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.statix.Model
import java.util.UUID

object Users : UUIDTable() {
}

@Model //kser example
class User(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object: EntityClass<UUID, User>(Users)
}