package ord.pumped.io.websocket

import io.ktor.server.application.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import ord.pumped.io.websocket.auth.IWebsocketAuthenticator
import ord.pumped.io.websocket.routing.IWebsocketRouter
import ord.pumped.io.websocket.routing.messaging.BadRequestNotification
import ord.pumped.io.websocket.routing.messaging.IWebsocketNotification
import ord.pumped.usecase.user.domain.model.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class WebsocketHandlerAdapter: IWebsocketHandler, KoinComponent {

    private val websocketAuthenticator by inject<IWebsocketAuthenticator>()
    private val websocketRouter by inject<IWebsocketRouter>()

    private val websockets = mutableMapOf<User, DefaultWebSocketSession>()
    private val websocketCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override suspend fun handleNewWebsocket(session: DefaultWebSocketSession, call: ApplicationCall) {
        val user = websocketAuthenticator.authenticate(call) ?: return session.close(unauthorizedCloseReason)

        return coroutineScope {
            async {
                try {
                    for (frame in session.incoming) {
                        frame as? Frame.Text ?: continue
                        val text = frame.readText()

                        val websocketAction = decodeActionFromString(text)

                        if (websocketAction == null) {
                            sendToSession(session, BadRequestNotification())
                            continue
                        }

                        val data = json.parseToJsonElement(text).jsonObject["data"]

                        if (data == null) {
                            sendToSession(session, BadRequestNotification())
                            continue
                        }

                        val notifications = websocketRouter.routePath(websocketAction.path, data.jsonObject)

                        notifications.forEach {
                            sendToSession(session, it)
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                } finally {
                    close(user)
                }
            }
        }
    }

    override fun registerNewWebsocket(
        user: User,
        session: DefaultWebSocketSession
    ) {
        websockets[user] = session
    }

    override fun sendNotificationToUser(user: User, notification: IWebsocketNotification) {
        val session = websockets[user] ?: return
        sendToSession(session, notification)
    }

    fun sendToSession(session: DefaultWebSocketSession, value: IWebsocketNotification) {
        sendStringToSessionAsync(session, json.encodeToString(value))
    }

    private fun sendStringToSessionAsync(session: DefaultWebSocketSession, string: String) {
        websocketCoroutineScope.async {
            session.send(string)
        }
    }

    override fun close(user: User) {
        runBlocking {
            websockets[user]?.close(defaultCloseReason)
        }
        websockets.remove(user)
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
data class DefaultWebsocketAction(val path: String)

private val unauthorizedCloseReason = CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Unauthorized")
private val defaultCloseReason = CloseReason(CloseReason.Codes.NORMAL, "Closes by host")