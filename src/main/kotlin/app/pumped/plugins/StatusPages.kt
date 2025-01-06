package app.pumped.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<RequestValidationException> { call: ApplicationCall, cause: RequestValidationException ->
            call.respond(status = HttpStatusCode.BadRequest, ValidationResponse("Validation failed", cause.reasons))
        }
    }
}

@Serializable
private data class ValidationResponse(val message: String, val reasons: List<String>)