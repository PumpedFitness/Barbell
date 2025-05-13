package ord.pumped.io.websocket.routing

import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonObject
import ord.pumped.io.websocket.routing.messaging.IWebsocketAction
import ord.pumped.io.websocket.routing.messaging.IWebsocketResponse
import ord.pumped.usecase.user.domain.model.User
import org.koin.ktor.ext.inject

interface IWebsocketRouter {
    fun defineRoute(route: IWebsocketRoute<out IWebsocketAction>)

    fun routePath(path: String, eventData: JsonObject, user: User): IWebsocketResponse?
}

inline fun <reified T: IWebsocketAction> Route.routeWebsocket(path: String, noinline block: IWebsocketRoute<T>.(action: T, user: User) -> IWebsocketResponse?): IWebsocketRoute<T> {
    val router by application.inject<IWebsocketRouter>()
    val route = DefaultWebsocketRoute(path, T::class, block)

    router.defineRoute(route)
    return route
}