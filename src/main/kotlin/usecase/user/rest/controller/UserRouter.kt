package ord.pumped.usecase.user.rest.controller

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import ord.pumped.common.security.service.SecurityController
import ord.pumped.configuration.tokenID
import ord.pumped.configuration.userID
import ord.pumped.configuration.userTokenCookie
import ord.pumped.io.websocket.routing.messaging.IWebsocketAction
import ord.pumped.io.websocket.routing.messaging.UserProfileNotification
import ord.pumped.io.websocket.routing.routeWebsocket
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

            call.response.cookies.append(userTokenCookie(response.token!!))
            call.respond(HttpStatusCode.OK, response)
        }
    }
    routeWebsocket<GetMeAction>("/api/v1/me") { _, user ->
        val response = UserController.getMe(user.id!!)
        UserProfileNotification(response.email)
    }
}


@Serializable
data class GetMeAction(val holder: String = ""): IWebsocketAction

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
            SecurityController.blacklistToken(call.tokenID())
            call.respond(HttpStatusCode.OK)
        }

        route("/update") {
            put("/password") {
                UserController.updatePassword(call.userID(), call.receive<UserUpdatePasswordRequest>())
                call.respond(HttpStatusCode.OK)
            }
        }

        delete("/logout") {
            UserController.logoutUser(call.tokenID())
            call.respond(HttpStatusCode.OK)
        }

    }

}