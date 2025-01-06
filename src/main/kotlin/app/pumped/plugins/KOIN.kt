package app.pumped.plugins

import app.pumped.data.context.KoinRouteContext
import app.pumped.data.context.RouteContext
import app.pumped.domain.user.UserRepository
import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger


fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}

private val appModule = module {
    singleOf(::UserRepository)
    singleOf(::JWTService)
    scope<RouteContext> {
        named(Scope.CALL_PIPELINE.value)
        scoped { (call: ApplicationCall) -> KoinRouteContext(call) }
    }
}