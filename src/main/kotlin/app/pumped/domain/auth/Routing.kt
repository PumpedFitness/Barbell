package app.pumped.domain.auth

import app.pumped.domain.auth.protected.protectedAuthRouting
import app.pumped.domain.auth.unprotected.authRouting
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.authRouting() {
    route("/auth") {
        unprotectedAuthRouting()
        protectedAuthRouting()
    }
}

private fun Route.unprotectedAuthRouting() {
    authRouting()
}


