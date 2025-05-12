package ord.pumped.io.websocket.routing.messaging

interface IWebsocketNotification: IWebsocketResponse {
    val priority: NotificationPriority
    override val shouldNotify: Boolean
        get() = true

    val body: String
    val title: String
}