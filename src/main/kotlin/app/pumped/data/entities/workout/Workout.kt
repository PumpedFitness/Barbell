package app.pumped.data.entities.workout

import app.pumped.data.entities.exercise.Exercises
import app.pumped.data.entities.socialstatistic.SocialStatistics
import app.pumped.domain.user.Users
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object Workouts : UUIDTable() {
    val comment = varchar("comment", 255)
    val startTime = datetime("startTime")
    val endTime = datetime("startTime")
    val owner = reference("owner", Users.id)
    val socials = reference("socials", SocialStatistics.id)
}

@Model //kser example
class Workout(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, Workout>(Workouts)

    var comment by Workouts.comment
    val startTime by Workouts.startTime
    val endTime by Workouts.endTime
    val owner by Workouts.owner
    val socials by Workouts.socials
}

object WorkoutExercises : UUIDTable() {
    val workout = reference("workout", Workouts.id)
    val exercise = reference("exercise", Exercises.id)
}

@Model //kser example
class WorkoutExercise(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, WorkoutExercise>(WorkoutExercises)

    val workout by WorkoutExercises.workout
    val exercise by WorkoutExercises.exercise
}


