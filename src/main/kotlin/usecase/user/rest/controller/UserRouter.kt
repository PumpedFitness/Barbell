package ord.pumped.usecase.user.rest.controller

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ord.pumped.io.validation.receiveAPIRequest
import ord.pumped.usecase.user.rest.request.UserRegisterRequest


fun Route.userRouting() {
    route("/user") {
        post("/register") {
            UserController.registerUser(
                call.receiveAPIRequest<UserRegisterRequest>()
            )

            call.respond(HttpStatusCode.OK)
        }
    }
}