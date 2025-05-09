package ord.pumped.io.websocket.routing.adapter

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer
import ord.pumped.io.websocket.routing.IWebsocketRoute
import ord.pumped.io.websocket.routing.IWebsocketRouter
import ord.pumped.io.websocket.routing.messaging.IWebsocketAction
import ord.pumped.io.websocket.routing.messaging.IWebsocketNotification
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

class WebsocketRouterAdapter: IWebsocketRouter {

    private val registeredRoutes = mutableSetOf<IWebsocketRoute<out IWebsocketAction>>()
    private val json = Json { ignoreUnknownKeys = true }

    override fun defineRoute(route: IWebsocketRoute<out IWebsocketAction>) {
        registeredRoutes.add(route)
    }

    override fun routePath(path: String, eventData: JsonObject): List<IWebsocketNotification> {
        val notifications = mutableListOf<IWebsocketNotification>()
        registeredRoutes.forEach { route ->

            val serializer = serializerByKClass(route.actionType)!!
            val action = json.decodeFromJsonElement(serializer, eventData)

            route as IWebsocketRoute<IWebsocketAction>

            if (route.path == path) {
                route.execute(action)?.let { notifications.add(it) }
            }
        }
        return notifications
    }

    fun <T : Any> serializerByKClass(kClass: KClass<T>): DeserializationStrategy<T>? {
        @Suppress("UNCHECKED_CAST")
        return serializer(kClass.createType()) as? DeserializationStrategy<T>
    }
}