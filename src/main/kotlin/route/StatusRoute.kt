package ord.pumped.routes

import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.statusRouting() {
    get("/status") {
        call.respondText("OK")
    }
}