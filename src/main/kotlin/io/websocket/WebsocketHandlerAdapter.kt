package ord.pumped.io.websocket

import io.ktor.server.application.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import ord.pumped.io.websocket.auth.IWebsocketAuthenticator
import ord.pumped.io.websocket.routing.IWebsocketRouter
import ord.pumped.io.websocket.routing.messaging.BadRequestNotification
import ord.pumped.io.websocket.routing.messaging.IWebsocketNotification
import ord.pumped.io.websocket.routing.messaging.IWebsocketResponse
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.util.toUUIDOrNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*


class WebsocketHandlerAdapter: IWebsocketHandler, KoinComponent {

    private val websocketAuthenticator by inject<IWebsocketAuthenticator>()
    private val websocketRouter by inject<IWebsocketRouter>()

    private val websockets = mutableMapOf<UUID, DefaultWebSocketSession>()
    private val websocketCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override suspend fun handleNewWebsocket(session: DefaultWebSocketSession, call: ApplicationCall) {
        val user = websocketAuthenticator.authenticate(call) ?: return session.close(unauthorizedCloseReason)

        registerNewWebsocket(user, session)

        return coroutineScope {
            async {
                try {
                    wsLoop@ for (frame in session.incoming) {
                        frame as? Frame.Text ?: continue
                        val text = frame.readText()

                        val websocketAction = decodeActionFromString(text) ?: run {
                            sendToSession(session, BadRequestNotification(message = "Cant parse provided data!"))
                            continue@wsLoop
                        }

                        val data = json.parseToJsonElement(text).jsonObject["data"] ?: run {
                            sendToSession(session, BadRequestNotification(message = "Missing data attribute"))
                            continue
                        }

                        val uuid = websocketAction.id.toUUIDOrNull() ?: run {
                            sendToSession(session, BadRequestNotification(message = "Missing id attribute"))
                            continue@wsLoop
                        }

                        val notifications = websocketRouter.routePath(websocketAction.path, data.jsonObject, user)

                        notifications?.let { notifySession(session, it, uuid) }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                } finally {
                    close(user.id!!)
                }
            }
        }
    }

    override fun registerNewWebsocket(
        user: User,
        session: DefaultWebSocketSession
    ) {
        if (websockets.keys.any { it == user.id }) {
            return
        }

        websockets[user.id!!] = session
    }

    override fun sendNotificationToUser(uuid: UUID, notification: IWebsocketNotification) {
        val session = websockets[uuid] ?: return
        sendToSession(session, notification)
    }

    override fun getOnlineUsers(): List<UUID> {
        return websockets.keys.toList()
    }

    override fun sendNotificationToAllUsers(notification: IWebsocketNotification) {
        websockets.keys.forEach { sendNotificationToUser(it, notification) }
    }

    fun notifySession(session: DefaultWebSocketSession, notification: IWebsocketResponse, id: UUID) {
        val encodedNotification = notification.asJson()

        val idInjectedJSON = JsonObject(encodedNotification.jsonObject + ("id" to JsonPrimitive(id.toString())))

        sendStringToSessionAsync(session, idInjectedJSON.toString())
    }

    fun sendToSession(session: DefaultWebSocketSession, value: IWebsocketResponse) {
        sendStringToSessionAsync(session, value.asJson().toString())
    }

    private fun sendStringToSessionAsync(session: DefaultWebSocketSession, string: String) {
        websocketCoroutineScope.async {
            session.send(string)
        }
    }

    override fun close(uuid: UUID) {
        runBlocking {
            websockets[uuid]?.close(defaultCloseReason)
        }
        websockets.remove(uuid)
    }

    private fun decodeActionFromString(content: String): DefaultWebsocketAction? {
        return try {
            json.decodeFromString<DefaultWebsocketAction>(content)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@Serializable
data class DefaultWebsocketAction(
    val path: String,
    val id: String,
)

private val unauthorizedCloseReason = CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Unauthorized")
private val defaultCloseReason = CloseReason(CloseReason.Codes.NORMAL, "Closes by host")