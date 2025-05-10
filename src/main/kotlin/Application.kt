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
    println("Starting application")

    print(System.getenv("BB_SECRET_ADAPTER"))
    print(System.getenv("BB_VAULT_HOST"))
    print(System.getenv("BB_VAULT_TOKEN"))
    print(System.getenv("BB_VAULT_PATH"))
    println("configuring Secrets")
    configureSecrets()

    print("connect DB")
    configureDatabases(testing)
    print("connect RabbitMQ")
    configureRabbitMQ()

    print("configure application")
    configureKoin()
    configureHTTP()
    configureSecurity()
    configureSerialization()
    configureAdministration()
    configureAkkurate()
    configureRouting()
    configureRoutes()
    configureMiddlewares()

    configureOpenAPI()
    configureSwagger()
}
