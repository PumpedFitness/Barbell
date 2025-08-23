package ord.pumped.io.websocket.routing

import ord.pumped.io.websocket.routing.messaging.IWebsocketAction
import ord.pumped.io.websocket.routing.messaging.IWebsocketResponse
import ord.pumped.usecase.user.domain.model.User
import kotlin.reflect.KClass

interface IWebsocketRoute<Action: IWebsocketAction> {
    val actionType: KClass<Action>
    val path: String
    fun execute(action: Action, user: User): IWebsocketResponse?
}

class DefaultWebsocketRoute<A: IWebsocketAction>(
    override val path: String,
    override val actionType: KClass<A>,
    val block: IWebsocketRoute<A>.(action: A, user: User) -> IWebsocketResponse?
): IWebsocketRoute<A> {

    override fun execute(action: A, user: User): IWebsocketResponse? {
        return block(this, action, user)
    }
}