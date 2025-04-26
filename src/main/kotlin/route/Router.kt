package ord.pumped.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ord.pumped.route.api.apiRouting

fun Application.configureRoutes() {
    routing {
        statusRouting()
        apiRouting()
    }
}