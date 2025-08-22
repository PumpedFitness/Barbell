package ord.pumped.configuration

import io.ktor.server.application.*
import ord.pumped.configuration.database.DatabaseAdapterFetcher
import ord.pumped.io.env.EnvVariables
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    log.info("Initializing database...")

    val adapter = DatabaseAdapterFetcher.fetchAdapterForDB(secrets[EnvVariables.BB_DB_TYPE])

    val datasource = adapter.asDataSource(this)

    Database.connect(datasource)
    configureMigrations(datasource)
}
