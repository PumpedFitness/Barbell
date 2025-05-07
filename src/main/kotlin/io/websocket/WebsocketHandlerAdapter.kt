package ord.pumped.io.websocket

import io.ktor.server.application.*
import io.ktor.websocket.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import ord.pumped.io.websocket.auth.IWebSocketAuthenticator
import ord.pumped.usecase.user.domain.model.User
import org.koin.core.component.inject


class WebsocketHandlerAdapter: IWebsocketHandler {

    private val websocketAuthenticator by inject<IWebSocketAuthenticator>()
    private val websockets = mutableMapOf<User, DefaultWebSocketSession>()

    override suspend fun handleNewWebsocket(session: DefaultWebSocketSession, call: ApplicationCall) {
        val user = websocketAuthenticator.authenticate(call) ?: return session.close(unauthorizedCloseReason)

        return coroutineScope {
            async {
                try {
                    for (frame in session.incoming) {
                        frame as? Frame.Text ?: continue
                        val text = frame.readText()

                        //TODO Add routing
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

    override fun sendNotificationToUser(user: User) {
        val session = websockets[user] ?: return
        //TODO add this and notification system
        //session.send()
    }

    override fun close(user: User) {
        runBlocking {
            websockets[user]?.close(defaultCloseReason)
        }
        websockets.remove(user)
    }
}

private val unauthorizedCloseReason = CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Unauthorized")
private val defaultCloseReason = CloseReason(CloseReason.Codes.NORMAL, "Closes by host")