package ord.pumped.usecase.user.rest.controller

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ord.pumped.common.request.actions.EmptyAction
import ord.pumped.common.response.EmptyResponse
import ord.pumped.common.security.service.SecurityController
import ord.pumped.configuration.secrets
import ord.pumped.configuration.tokenID
import ord.pumped.configuration.userID
import ord.pumped.configuration.userTokenCookie
import ord.pumped.io.env.EnvVariables
import ord.pumped.io.websocket.WebsocketController
import ord.pumped.io.websocket.routing.routeWebsocket
import ord.pumped.usecase.user.rest.request.*
import ord.pumped.usecase.user.rest.request.actions.NotifyUserAction

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

            val domain = application.secrets[EnvVariables.BB_JWT_DOMAIN]
            val isSecure = application.secrets[EnvVariables.BB_MODE] == "PRODUCTION"

            call.response.cookies.append(userTokenCookie(response.token!!, domain, isSecure))
            call.respond(HttpStatusCode.OK, response)
        }
    }
    routeWebsocket<EmptyAction>("/api/v1/list_users") { _, _ ->
        UserController.getOnlineUsers()
    }
    routeWebsocket<NotifyUserAction>("/api/v1/notify_user") { action, user ->
        UserController.notifyUser(action.userID, user)
        EmptyResponse()
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
            WebsocketController.destroyWebsocket(call.userID())
            call.respond(HttpStatusCode.OK)
        }
    }
}