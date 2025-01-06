package app.pumped.plugins

import app.pumped.api.v1.apiV1Routing
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("OK")
        }

        apiV1Routing()
    }
}
