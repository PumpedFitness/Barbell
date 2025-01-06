package app.pumped.plugins

import app.pumped.api.APIRequest
import app.pumped.data.context.KoinRouteContext
import app.pumped.data.context.RouteContext
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.koin.ktor.ext.getKoin
import statix.org.*

fun Application.configureMiddleware() {
    install(Middlewares) {
        this.middleware = globalMiddleware
    }
}

private val globalMiddleware = middlewareGroup {
    //this += ContextMiddleware()
    // this += ValidateRequestMiddleware()
}

private class ContextMiddleware: Middleware {
    override suspend fun handleCall(call: ApplicationCall, receives: MiddlewareData?): MiddlewareData {
        call.getKoin().createScope<RouteContext>(Scope.CALL_PIPELINE.value, KoinRouteContext(call))
        return MiddlewareData.empty()
    }
}

private class ValidateRequestMiddleware: Middleware {
    override suspend fun handleCall(call: ApplicationCall, receives: MiddlewareData?): MiddlewareData {
        val data = call.receive<APIRequest>()
        if (data is APIRequest) {
            val result = data.validate()
            if (result is ValidationResult.Invalid) {
                throw RequestValidationException(data, result.reasons)
            }
        }
        return MiddlewareData.empty()
    }
}