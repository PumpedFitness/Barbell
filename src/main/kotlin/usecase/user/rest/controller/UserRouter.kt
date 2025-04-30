package ord.pumped.usecase.user.rest.controller

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ord.pumped.configuration.userID
import ord.pumped.usecase.user.rest.request.UserLoginRequest
import ord.pumped.usecase.user.rest.request.UserRegisterRequest
import ord.pumped.usecase.user.rest.request.UserUpdateProfileRequest

fun Route.userRoutingUnauthed() {
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
    }
}

fun Route.userRoutingAuthed() {
    route("/user") {

        route("/profile") {

            post("/update") {
                val userID = call.userID()
                val response = UserProfileController.postUserProfile(userID, call.receive<UserUpdateProfileRequest>())

                call.respond(HttpStatusCode.OK, response)
            }

            get("/me") {
                val userID = call.userID()
                val response = UserController.getMe(userID)
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}