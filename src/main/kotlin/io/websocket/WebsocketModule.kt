package ord.pumped.io.websocket

import ord.pumped.io.websocket.auth.IWebsocketAuthenticator
import ord.pumped.io.websocket.auth.WebsocketAuthenticatorAdapter
import ord.pumped.io.websocket.auth.WebsocketUpgradeServiceAdapter
import ord.pumped.io.websocket.auth.controller.IWebsocketUpgradeService
import ord.pumped.io.websocket.routing.IWebsocketRouter
import ord.pumped.io.websocket.routing.adapter.WebsocketRouterAdapter
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val websocketModule = module {
    singleOf(::WebsocketAuthenticatorAdapter) { bind<IWebsocketAuthenticator>() }
    singleOf(::WebsocketHandlerAdapter) { bind<IWebsocketHandler>() }
    singleOf(::WebsocketRouterAdapter) { bind<IWebsocketRouter>() }
    singleOf(::WebsocketUpgradeServiceAdapter) { bind<IWebsocketUpgradeService>() }
}