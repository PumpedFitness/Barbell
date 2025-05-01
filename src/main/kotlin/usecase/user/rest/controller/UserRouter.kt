package ord.pumped.usecase.user.rest.controller

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ord.pumped.configuration.userID
import ord.pumped.usecase.user.rest.request.*

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
            route("/update") {
                put {
                    val response =
                        UserProfileController.postUserProfile(call.userID(), call.receive<UserUpdateProfileRequest>())
                    call.respond(HttpStatusCode.OK, response)
                }
            }

            get("/me") {
                val response = UserController.getMe(call.userID())
                call.respond(HttpStatusCode.OK, response)
            }

        }

        delete("/delete") {
            UserController.deleteUser(call.userID(), call.receive<UserDeleteUserRequest>())
            call.respond(HttpStatusCode.OK)
        }

        route("/update") {
            put("/password") {
                UserController.updatePassword(call.userID(), call.receive<UserUpdatePasswordRequest>())
                call.respond(HttpStatusCode.OK)
            }
        }

    }
}