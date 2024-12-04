package app.pumped.plugins

import io.ktor.server.application.*
import statix.org.Middleware
import statix.org.MiddlewareData
import statix.org.Middlewares

fun Application.configureMiddleware() {
    install(Middlewares) {
        this.middleware = EmptyMiddleware()
    }
}

private class EmptyMiddleware(): Middleware {
    override suspend fun handleCall(call: ApplicationCall, receives: MiddlewareData?): MiddlewareData {
        return MiddlewareData.empty()
    }
}