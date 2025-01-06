package app.pumped.domain.auth.unprotected

import app.pumped.api.requests.auth.LoginRequest
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.loginRouting() {
    post("/login") {
        val request = call.receive<LoginRequest>()



    }
}