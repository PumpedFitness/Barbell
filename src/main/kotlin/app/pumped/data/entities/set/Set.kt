package app.pumped.data.entities.set

import app.pumped.data.entities.workoutexercise.WorkoutExercises
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object Sets : UUIDTable() {
    val workoutExercise = reference("workoutExercise", WorkoutExercises.id)
    val number = short("number") //The nth set of this exercise
    val repetitions = short("repetitions")
    val weight = short("weight")
}

@Model //kser example
class Set(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, Set>(Sets)

    val workoutExercise by Sets.workoutExercise
    val number by Sets.number
    val repetitions by Sets.repetitions
    val weight by Sets.weight
}