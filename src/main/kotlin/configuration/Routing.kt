package ord.pumped.configuration

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import ord.pumped.common.APIException

fun Application.configureRouting() {
    install(RequestValidation)
    install(Resources)

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
        exception<APIException> { call, cause ->
            cause.handle(call)
        }
    }
}
