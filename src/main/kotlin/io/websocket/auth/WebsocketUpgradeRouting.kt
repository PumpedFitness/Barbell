package ord.pumped.io.websocket.auth

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import ord.pumped.io.websocket.auth.controller.IWebsocketUpgradeController
import ord.pumped.io.websocket.auth.request.UpgradeWebsocketRequest
import ord.pumped.usecase.user.domain.mapper.UserModelMapper
import ord.pumped.usecase.user.persistence.repository.UserRepository
import ord.pumped.util.toUUIDOrNull
import org.koin.ktor.ext.inject

fun Route.websocketAuth() {
    route("/websocket/upgrade") {
        post {
            val request = call.receive<UpgradeWebsocketRequest>()
            val id = request.socketID.toUUIDOrNull() ?: return@post call.respondText("Invalid UUID", status = HttpStatusCode.BadRequest)

            val userRepo by inject<UserRepository>()
            val userMapper by inject<UserModelMapper>()
            val websocketUpgradeController by inject<IWebsocketUpgradeController>()

            val userDTO = userRepo.findByID(id) ?: return@post call.respondText("Invalid User", status = HttpStatusCode.BadRequest)

            val user = userMapper.toDomain(userDTO)

            websocketUpgradeController.upgradeWebsocket(id, user)
            call.respondText("OK")
        }
    }
}