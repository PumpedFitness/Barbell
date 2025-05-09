package ord.pumped.io.websocket

import ord.pumped.io.websocket.auth.IWebSocketAuthenticator
import ord.pumped.io.websocket.auth.WebSocketAuthenticatorAdapter
import ord.pumped.io.websocket.routing.IWebsocketRouter
import ord.pumped.io.websocket.routing.adapter.WebsocketRouterAdapter
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val websocketModule = module {
    singleOf(::WebSocketAuthenticatorAdapter) { bind<IWebSocketAuthenticator>() }
    singleOf(::WebsocketHandlerAdapter) { bind<IWebsocketHandler>() }
    singleOf(::WebsocketRouterAdapter) { bind<IWebsocketRouter>() }
}