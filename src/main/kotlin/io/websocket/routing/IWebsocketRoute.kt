package ord.pumped.io.websocket.routing

import ord.pumped.io.websocket.routing.messaging.IWebsocketAction
import ord.pumped.io.websocket.routing.messaging.IWebsocketNotification
import kotlin.reflect.KClass

interface IWebsocketRoute<Action: IWebsocketAction> {
    val actionType: KClass<Action>
    val path: String
    fun execute(action: Action): IWebsocketNotification?
}

class DefaultWebsocketRoute<A: IWebsocketAction>(
    override val path: String,
    override val actionType: KClass<A>,
    val block: IWebsocketRoute<A>.(action: A) -> IWebsocketNotification?
): IWebsocketRoute<A> {

    override fun execute(action: A): IWebsocketNotification? {
        return block(this, action)
    }
}