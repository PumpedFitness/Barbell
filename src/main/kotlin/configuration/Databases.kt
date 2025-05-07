package ord.pumped.configuration

import io.ktor.server.application.*
import ord.pumped.configuration.database.DatabaseAdapterFetcher
import ord.pumped.configuration.database.adapters.H2Adapter
import ord.pumped.io.env.EnvVariables
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases(testing: Boolean = false) {
    log.info("Initializing database...")

    val adapter = if (testing) {
        log.info("Using H2 for DB")
        H2Adapter()
    } else {
        DatabaseAdapterFetcher.fetchAdapterForDB(secrets[EnvVariables.BB_DB_TYPE])
    }

    val datasource = adapter.asDataSource(this)

    Database.connect(datasource)
    configureMigrations(datasource)
}
