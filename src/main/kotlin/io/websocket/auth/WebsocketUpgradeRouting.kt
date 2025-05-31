package ord.pumped.io.websocket.auth

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import ord.pumped.configuration.userID
import ord.pumped.io.websocket.WebsocketController
import ord.pumped.io.websocket.auth.request.UpgradeWebsocketRequest
import ord.pumped.util.toUUIDOrNull

fun Route.websocketAuth() {
    route("/websocket/upgrade") {
        post {
            val request = call.receive<UpgradeWebsocketRequest>()
            val id = request.socketID.toUUIDOrNull() ?: return@post call.respondText("Invalid UUID", status = HttpStatusCode.BadRequest)

            WebsocketController.upgradeWebsocket(id, call.userID())
            call.respondText("OK", status = HttpStatusCode.OK)
        }
    }
}