package ord.pumped.route.api

import io.ktor.server.auth.*
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import ord.pumped.io.defaultJson
import ord.pumped.io.websocket.IWebsocketHandler
import ord.pumped.io.websocket.routing.messaging.IWebsocketNotification
import ord.pumped.route.api.v1.apiV1RoutingAuthed
import ord.pumped.route.api.v1.apiV1RoutingUnauthed
import org.koin.ktor.ext.inject

fun Route.apiRouting() {
        route("/api") {

            post("send_to_all") {
                @Serializable
                data class SendToAll(val title: String, val body: String)

                val info = call.receive<SendToAll>()

                val websocketHandler by application.inject<IWebsocketHandler>()

                websocketHandler.sendNotificationToAllUsers(TriggerNotification(info.title, info.body))
                call.respondText { "OK" }
            }

            route("/v1") {
                authenticate("jwt") {
                    apiV1RoutingAuthed()
                }
                apiV1RoutingUnauthed()
            }
        }
}

@Serializable
data class TriggerNotification(val title: String,
                               val body: String,
                               override val message: String = "Message",
                               override val status: Boolean = true
): IWebsocketNotification {
    override fun asJson(): JsonElement {
        return defaultJson.encodeToJsonElement(serializer(), this)
    }
}