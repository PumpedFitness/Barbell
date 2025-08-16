package ord.pumped.configuration

import io.ktor.server.application.*
import ord.pumped.io.env.EnvVariables
import org.flywaydb.core.Flyway
import javax.sql.DataSource

fun Application.configureMigrations(dataSource: DataSource) {
    log.info("Migrating database...")
    val devAlters = secrets[EnvVariables.BB_MODE]
        .takeIf { it == "DEV" }
        ?.let {
            """
                ALTER TABLE barbell_users
                    MODIFY COLUMN id BINARY(36) NOT NULL;
                ALTER TABLE barbell_jwt_tokens
                    MODIFY COLUMN id BINARY(36) NOT NULL;
            """.trimIndent()
        } ?: ""

    try {
        val flyway = Flyway
            .configure()
            .validateMigrationNaming(true)
            .dataSource(dataSource)
            .placeholders(
                mapOf(
                    "DEV_ALTERS" to devAlters
                )
            )
            .load()
        flyway.migrate()
    } catch (e: Exception) {
        log.error(e.message)
    }
    log.info("Migration done.")
}