package ord.pumped.configuration

import io.ktor.server.application.*
import ord.pumped.usecase.user.persistence.dto.UsersTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.pingDatabase(): Boolean {
    log.info("Pinging database...")
    return try {
        transaction {
            UsersTable.selectAll()
        }
        true
    } catch (_: Throwable) {
        false
    }
}