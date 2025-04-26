package ord.pumped.route.api

import io.ktor.server.auth.authenticate
import io.ktor.server.routing.*
import ord.pumped.routes.api.v1.apiV1Routing

fun Route.apiRouting() {
    authenticate("jwt") {
        route("/api") {
            route("/v1") {
                apiV1Routing()
            }
        }
    }
}