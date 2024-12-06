package app.pumped.persistance

import app.pumped.io.env.EnvVariables
import app.pumped.plugins.globalEnv
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    connectToDb()
}

private fun connectToDb(): Boolean {
    val result = Database.connect(
        "jdbc:postgresql://${globalEnv[EnvVariables.BB_DB_HOST]}:${globalEnv[EnvVariables.BB_DB_PORT]}/${globalEnv[EnvVariables.BB_DB_DATABASE]}",
        driver = "org.postgresql.Driver",
        user = globalEnv[EnvVariables.BB_DB_USER],
        password = globalEnv[EnvVariables.BB_DB_PASSWORD]
    )
    return true
}