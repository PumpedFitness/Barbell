package ord.pumped.io.websocket.routing.adapter

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer
import ord.pumped.io.websocket.routing.IWebsocketRoute
import ord.pumped.io.websocket.routing.IWebsocketRouter
import ord.pumped.io.websocket.routing.messaging.IWebsocketAction
import ord.pumped.io.websocket.routing.messaging.IWebsocketResponse
import ord.pumped.usecase.user.domain.model.User
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

class WebsocketRouterAdapter: IWebsocketRouter {

    private val registeredRoutes = mutableSetOf<IWebsocketRoute<out IWebsocketAction>>()
    private val json = Json { ignoreUnknownKeys = true }

    override fun defineRoute(route: IWebsocketRoute<out IWebsocketAction>) {
        registeredRoutes.add(route)
    }

    override fun routePath(path: String, eventData: JsonObject, user: User): IWebsocketResponse? {
        val route = registeredRoutes.find { it.path == path } ?: return null

        val serializer = serializerByKClass(route.actionType)!!
        val action = json.decodeFromJsonElement(serializer, eventData)

        route as IWebsocketRoute<IWebsocketAction>

        return route.execute(action, user)
    }

    fun <T : Any> serializerByKClass(kClass: KClass<T>): DeserializationStrategy<T>? {
        @Suppress("UNCHECKED_CAST")
        return serializer(kClass.createType()) as? DeserializationStrategy<T>
    }
}