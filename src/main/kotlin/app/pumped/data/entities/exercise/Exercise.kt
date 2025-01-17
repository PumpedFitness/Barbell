package app.pumped.data.entities.exercise

import app.pumped.data.entities.socialstatistic.Socialstatistics
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object Exercises : UUIDTable() {
    val socials = reference("socials", Socialstatistics.id)
    val muscleGroup = enumeration("muscleGroup", MuscleGroup::class)
}
enum class MuscleGroup {
    CHEST,
    BACK,
    LAT,
    ABDOMINAL,
    SHOULDER,
    BICEPS,
    TRICEPS,
    FOREARMS,
    QUADS,
    GLUTE,
    HAMSTRING,
    CALVES
}
@Model //kser example
class Exercise(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, Exercise>(Exercises)

}