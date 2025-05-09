package ord.pumped.io.websocket

import io.ktor.server.application.*
import io.ktor.websocket.*
import ord.pumped.io.websocket.routing.messaging.IWebsocketNotification
import ord.pumped.usecase.user.domain.model.User
import org.koin.core.component.KoinComponent

interface IWebsocketHandler: KoinComponent {
    suspend fun handleNewWebsocket(session: DefaultWebSocketSession, call: ApplicationCall)

    fun registerNewWebsocket(user: User, session: DefaultWebSocketSession)

    fun sendNotificationToUser(user: User, notification: IWebsocketNotification)

    fun close(user: User)
}