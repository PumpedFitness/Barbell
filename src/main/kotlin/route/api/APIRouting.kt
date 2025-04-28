package ord.pumped.route.api

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import ord.pumped.routes.api.v1.apiV1RoutingAuthed
import ord.pumped.routes.api.v1.apiV1RoutingUnauthed

fun Route.apiRouting() {
        route("/api") {
            route("/v1") {
                authenticate("jwt") {
                    apiV1RoutingAuthed()
                }
                apiV1RoutingUnauthed()
            }
        }
}