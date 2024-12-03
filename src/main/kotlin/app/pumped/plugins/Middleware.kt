package app.pumped.plugins

import io.ktor.server.application.*
import statix.org.Middlewares

fun Application.configureMiddleware() {
    install(Middlewares)
}