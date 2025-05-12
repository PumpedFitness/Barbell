package ord.pumped.io.websocket

import io.ktor.server.application.*
import io.ktor.websocket.*
import ord.pumped.io.websocket.routing.messaging.IWebsocketNotification
import ord.pumped.usecase.user.domain.model.User
import org.koin.core.component.KoinComponent
import java.util.UUID

interface IWebsocketHandler: KoinComponent {
    suspend fun handleNewWebsocket(session: DefaultWebSocketSession, call: ApplicationCall)

    fun registerNewWebsocket(user: User, session: DefaultWebSocketSession)

    fun sendNotificationToUser(uuid: UUID, notification: IWebsocketNotification)

    fun sendNotificationToAllUsers(notification: IWebsocketNotification)

    fun getOnlineUsers(): List<UUID>

    fun close(uuid: UUID)
}