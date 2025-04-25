package ord.pumped.routes.api

import io.ktor.server.routing.*
import ord.pumped.routes.api.v1.apiV1Routing

fun Route.apiRouting() {
    route("/api") {
        route("/v1") {
            apiV1Routing()
        }
    }
}