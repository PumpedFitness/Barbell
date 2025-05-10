package ord.pumped.route.api.v1

import io.ktor.server.routing.*
import ord.pumped.usecase.user.rest.controller.userRoutingAuthed
import ord.pumped.usecase.user.rest.controller.userRoutingUnauthed

fun Route.apiV1RoutingAuthed() {
    route("/auth") {
        userRoutingAuthed()
    }
}

fun Route.apiV1RoutingUnauthed() {
    userRoutingUnauthed()
}