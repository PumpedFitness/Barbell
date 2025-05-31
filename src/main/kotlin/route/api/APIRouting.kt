package ord.pumped.route.api

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import ord.pumped.io.websocket.auth.websocketAuth
import ord.pumped.route.api.v1.apiV1RoutingAuthed
import ord.pumped.route.api.v1.apiV1RoutingUnauthed

fun Route.apiRouting() {
        route("/api") {
            route("/v1") {
                authenticate("jwt") {
                    apiV1RoutingAuthed()
                    websocketAuth()
                }
                apiV1RoutingUnauthed()
            }
        }
}