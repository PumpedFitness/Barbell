package ord.pumped.usecase.user.rest.controller

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ord.pumped.usecase.user.rest.request.UserLoginRequest
import ord.pumped.usecase.user.rest.request.UserRegisterRequest


fun Route.userRouting() {
    route("/user") {
        post("/register") {
            val response = UserController.registerUser(
                call.receive<UserRegisterRequest>()
            )

            call.respond(HttpStatusCode.Created, response)
        }

        post("/login") {
            val response = UserController.loginUser(
                call.receive<UserLoginRequest>(),
                call.application
            )

            call.respond(HttpStatusCode.OK, response)
        }

        get("/me") {
            val userID = call.request.queryParameters["token"]
            //val response = UserController.getMe(token)
            call.respond(HttpStatusCode.OK)
        }
    }
}