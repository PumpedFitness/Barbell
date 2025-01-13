package app.pumped.domain.auth.protected

import app.pumped.domain.user.generated.toModel
import app.pumped.plugins.user
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.protectedAuthRouting() {
    authenticate("jwt") {
        get("/me") {
            val user = call.user() ?: return@get call.respond(HttpStatusCode.Unauthorized)
            call.respond(user.toModel())
        }
    }
}