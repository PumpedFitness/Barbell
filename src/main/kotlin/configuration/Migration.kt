package ord.pumped.configuration

import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import javax.sql.DataSource

fun Application.configureMigrations(dataSource: DataSource) {
    log.info("Migrating database...")

    try {
        val flyway = Flyway
            .configure()
            .validateMigrationNaming(true)
            .dataSource(dataSource)
            .load()
        flyway.migrate()
    } catch (e: Exception) {
        log.error(e.message)
    }
    log.info("Migration done.")
}