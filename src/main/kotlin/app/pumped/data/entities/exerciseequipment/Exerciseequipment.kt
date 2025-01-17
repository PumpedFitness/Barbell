package app.pumped.data.entities.exerciseequipment

import app.pumped.data.entities.equipment.Equipments
import app.pumped.data.entities.exercise.Exercises
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object Exerciseequipments : UUIDTable() {
    val exercise = reference("exercise", Exercises.id)
    val equipment = reference("equipment", Equipments.id)
}
@Model //kser example
class Exerciseequipment(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, Exerciseequipment>(Exerciseequipments)

}