package app.pumped.data.entities.workout

import app.pumped.data.entities.exercise.Exercises
import app.pumped.data.entities.socialstatistic.Socialstatistics
import app.pumped.domain.user.Users
import app.pumped.domain.user.Users.uniqueIndex
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object Workouts : UUIDTable() {
    val comment = varchar("comment", 255)
    val startTime = datetime("startTime")
    val endTime = datetime("startTime")
    val owner = reference("owner", Users.id)
    val socials = reference("socials", Socialstatistics.id)
}

@Model //kser example
class Workout(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, Workout>(Workouts)

    var comment by Workouts.comment
}



