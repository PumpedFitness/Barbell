package app.pumped

import app.pumped.data.persistance.configureDatabase
import app.pumped.plugins.*
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
    configureEnv()
    configureDatabase()
    configureSerialization()
    configureSecurity()
    configureKoin()
    configureCSRF()
    configureSessions()
    configureTaskScheduling()
    configureRouting()
    configureValidation()
    configureMiddleware()
}
