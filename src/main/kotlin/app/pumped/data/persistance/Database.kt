package app.pumped.data.persistance

import app.pumped.domain.user.Users
import app.pumped.io.env.EnvVariables
import app.pumped.plugins.globalEnv
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    log.info("Connecting to database")

    val config = HikariConfig().apply {
        username = globalEnv[EnvVariables.BB_DB_USER]
        password = globalEnv[EnvVariables.BB_DB_PASSWORD]
        jdbcUrl = "jdbc:postgresql://${globalEnv[EnvVariables.BB_DB_HOST]}:${globalEnv[EnvVariables.BB_DB_PORT]}/${globalEnv[EnvVariables.BB_DB_DATABASE]}"
        driverClassName = "org.postgresql.Driver"
    }

    Database.connect(HikariDataSource(config))
    loadDatabaseTables()
}


fun loadDatabaseTables() {
    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            Users
        )
    }
}