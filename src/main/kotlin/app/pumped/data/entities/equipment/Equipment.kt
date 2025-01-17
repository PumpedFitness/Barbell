package app.pumped.data.entities.equipment

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object Equipments : UUIDTable() {

}
@Model //kser example
class Equipment(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, Equipment>(Equipments)

}