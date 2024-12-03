package app.pumped

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

/**
 * @author Devin Fritz
 */
fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

/**
 * Configure KTOR
 */
fun Application.module() {
    configureSerialization()
    configureSecurity()
    configureCSRF()
    configureSessions()
    configureRouting()
}
