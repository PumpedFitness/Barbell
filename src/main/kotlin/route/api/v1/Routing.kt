package ord.pumped.routes.api.v1

import io.ktor.server.routing.*
import ord.pumped.usecase.user.rest.controller.userRouting

fun Route.apiV1Routing() {
    userRouting()
}