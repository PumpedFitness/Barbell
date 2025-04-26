package ord.pumped.configuration.middlewares

import io.ktor.server.application.*
import statix.org.Middlewares

fun Application.configureMiddlewares() {
    install(Middlewares) {
        middleware = globalMiddleware
    }
}