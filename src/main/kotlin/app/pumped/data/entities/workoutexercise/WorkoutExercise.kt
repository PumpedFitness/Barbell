package app.pumped.data.entities.workoutexercise

import app.pumped.data.entities.exercise.Exercises
import app.pumped.data.entities.workout.Workouts
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object WorkoutExercises : UUIDTable() {
    val workout = reference("workout", Workouts.id)
    val exercise = reference("exercise", Exercises.id)
}

@Model //kser example
class WorkoutExercise(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, WorkoutExercise>(WorkoutExercises)

}