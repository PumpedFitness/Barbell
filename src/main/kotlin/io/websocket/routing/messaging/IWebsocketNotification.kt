package ord.pumped.io.websocket.routing.messaging

interface IWebsocketNotification: IWebsocketResponse {
    val priority: NotificationPriority
    val body: String
    val title: String
}