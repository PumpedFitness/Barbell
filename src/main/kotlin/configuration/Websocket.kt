package ord.pumped.configuration

import io.ktor.server.application.*
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.websocket.*
import ord.pumped.io.websocket.IWebsocketHandler
import org.koin.ktor.ext.inject
import kotlin.time.Duration.Companion.seconds

fun Application.configureWebsocket() {
    install(WebSockets) {
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    val websocketHandler by inject<IWebsocketHandler>()

    routing {
        route("/api/v1") {
            webSocket("/ws") {
                websocketHandler.handleNewWebsocket(this, call)
            }
        }
    }
}