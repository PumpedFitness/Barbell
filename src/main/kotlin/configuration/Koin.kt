package ord.pumped.configuration

import io.ktor.server.application.*
import ord.pumped.common.security.service.securityModule
import ord.pumped.io.websocket.websocketModule
import ord.pumped.usecase.user.userModule
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(userModule, securityModule, websocketModule)
    }
}