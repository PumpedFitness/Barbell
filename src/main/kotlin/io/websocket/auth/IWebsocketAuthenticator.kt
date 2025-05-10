package ord.pumped.io.websocket.auth

import io.ktor.server.application.ApplicationCall
import ord.pumped.usecase.user.domain.model.User

fun interface IWebsocketAuthenticator {
    fun authenticate(call: ApplicationCall): User?
}