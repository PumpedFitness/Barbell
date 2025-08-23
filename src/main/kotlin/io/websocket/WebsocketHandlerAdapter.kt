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
import ord.pumped.io.websocket.auth.UUIDResponse
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

    private val websockets = mutableMapOf<UUID, BarbellWebsocket>()
    private val websocketCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override suspend fun handleNewWebsocket(session: DefaultWebSocketSession, call: ApplicationCall) {
        val logger = call.application.log

        val websocketSession = BarbellWebsocket(session, false)

        val id = registerNewWebsocket(websocketSession)
        sendUUIDNotification(session, id)

        logger.info("New websocket opened")

        return coroutineScope {
            async {
                try {
                    wsLoop@ for (frame in session.incoming) {
                        if (!isSessionAuthenticated(session)) {
                            close(session, unauthorizedCloseReason)
                            return@async
                        }

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

                         val user = getUserForSession(session) ?: return@async close(session, unauthorizedCloseReason)

                        val notifications = websocketRouter.routePath(websocketAction.path, data.jsonObject, user)
                        notifications?.let { notifySession(session, it, uuid) }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                } finally {
                    close(session)
                }
            }
        }
    }

    override fun registerNewWebsocket(
        session: BarbellWebsocket,
    ): UUID {
        var id = UUID.randomUUID()
        while (websockets[id] != null) {
            id = UUID.randomUUID()
        }

        websockets[id] = session
        return id
    }

    private fun sendUUIDNotification(session: DefaultWebSocketSession, id: UUID) {
        sendToSession(session, UUIDResponse(id.toString()))
    }

    override fun sendNotificationToUser(uuid: UUID, notification: IWebsocketNotification) {
        val session = websockets[getSessionIDForUser(uuid)] ?: return
        sendToSession(session.websocket, notification)
    }

    override fun getOnlineUsers(): List<UUID> {
        return websockets.values.mapNotNull { it.user }.map { it.id!! }
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

    private fun isSessionAuthenticated(session: DefaultWebSocketSession): Boolean {
        return websockets.values.firstOrNull { it.websocket == session }?.isAuthenticated ?: false
    }

    private fun getUserForSession(session: DefaultWebSocketSession): User? {
        return websockets.values.firstOrNull { it.websocket == session }?.user
    }

    override fun close(session: DefaultWebSocketSession, closeReason: CloseReason) {
        val socketsToClose = websockets.filter { it.value.websocket == session }

        runBlocking {
            socketsToClose.forEach {
                it.value.websocket.close(closeReason)
                websockets.remove(it.key)
            }
        }
    }

    override fun closeForUser(userID: UUID) {
        val socketID = getSessionIDForUser(userID)
        val socket = websockets[socketID]
        socket?.let { close(it.websocket, defaultCloseReason) }
    }

    private fun decodeActionFromString(content: String): DefaultWebsocketAction? {
        return try {
            json.decodeFromString<DefaultWebsocketAction>(content)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getSessionIDForUser(userID: UUID): UUID? {
        return websockets.filter { it.value.user?.id == userID }.keys.firstOrNull()
    }

    override fun hasSession(sessionID: UUID): Boolean {
        return websockets.keys.any { it == sessionID }
    }


    override fun associateUserWithSocketID(sessionID: UUID, user: User) {
        val websocket = websockets[sessionID] ?: return
        websocket.user = user
        websocket.isAuthenticated = true
    }
}

@Serializable
data class DefaultWebsocketAction(
    val path: String,
    val id: String,
)

data class BarbellWebsocket(
    val websocket: DefaultWebSocketSession,
    var isAuthenticated: Boolean,
    var user: User? = null,
)

private val unauthorizedCloseReason = CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Unauthorized")
val defaultCloseReason = CloseReason(CloseReason.Codes.NORMAL, "Closed by host")