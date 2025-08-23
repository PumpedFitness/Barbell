package ord.pumped

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import ord.pumped.configuration.*
import ord.pumped.configuration.middlewares.configureMiddlewares
import ord.pumped.routes.configureRoutes

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module(testing: Boolean = false) {
    configureSecrets(testing)

    configureDatabases()
    configureRabbitMQ()

    configureKoin()
    configureHTTP()
    configureSecurity()
    configureSerialization()
    configureAdministration()
    configureAkkurate()
    configureRouting()
    configureRoutes()
    configureMiddlewares()
    configureWebsocket()

    configureOpenAPI()
    configureSwagger()
}
