package app.pumped.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<RequestValidationException> { call: ApplicationCall, cause ->
            call.respondText(cause.message ?: "Validation failed", status = HttpStatusCode.BadRequest)
        }
    }
}