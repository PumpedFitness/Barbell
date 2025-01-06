package app.pumped.api.v1

import app.pumped.domain.auth.authRouting
import io.ktor.server.routing.*

fun Route.apiV1Routing() {
    route("/api/v1") {
        authRouting()
    }
}