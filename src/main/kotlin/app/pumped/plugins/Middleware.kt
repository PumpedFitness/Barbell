package app.pumped.plugins

import app.pumped.data.context.KoinRouteContext
import app.pumped.data.context.RouteContext
import io.ktor.server.application.*
import org.koin.ktor.ext.getKoin
import statix.org.*

fun Application.configureMiddleware() {
    install(Middlewares) {
        this.middleware = globalMiddleware
    }
}

private val globalMiddleware = middlewareGroup {
    this += ContextMiddleware()
}

private class ContextMiddleware: Middleware {
    override suspend fun handleCall(call: ApplicationCall, receives: MiddlewareData?): MiddlewareData {
        call.getKoin().createScope<RouteContext>(Scope.CALL_PIPELINE.value, KoinRouteContext(call))
        return MiddlewareData.empty()
    }
}