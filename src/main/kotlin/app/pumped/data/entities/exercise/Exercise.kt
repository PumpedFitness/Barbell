package app.pumped.data.entities.exercise

import app.pumped.data.entities.equipment.Equipments
import app.pumped.data.entities.socialstatistic.SocialStatistics
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object Exercises : UUIDTable() {
    val name = varchar("name", 128)
    val social = reference("social", SocialStatistics.id)
    val muscleGroup = reference("muscleGroups", ExerciseMuscleGroups.id)
}

@Model //kser example
class Exercise(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, Exercise>(Exercises)

    val name by Exercises.name
    val social by Exercises.social
    val muscleGroup by Exercises.muscleGroup

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

object ExerciseMuscleGroups : UUIDTable() {
    val muscleGroup = Exercises.enumeration("muscleGroup", MuscleGroup::class)
    val exercise = reference("exercise", Exercises.id)
}

@Model //kser example
class ExerciseMuscleGroup(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, ExerciseMuscleGroup>(ExerciseMuscleGroups)

    val exercise by ExerciseMuscleGroups.exercise
    val muscleGroup by ExerciseMuscleGroups.muscleGroup

}

object ExerciseEquipments : UUIDTable() {
    val exercise = reference("exercise", Exercises.id)
    val equipment = reference("equipment", Equipments.id)
}
@Model //kser example
class Exerciseequipment(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, Exerciseequipment>(ExerciseEquipments)

    val exercise by ExerciseEquipments.exercise
    val equipment by ExerciseEquipments.equipment
}
