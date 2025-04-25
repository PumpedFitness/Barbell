package ord.pumped.configuration

import dev.nesk.akkurate.ktor.server.Akkurate
import dev.nesk.akkurate.ktor.server.registerValidator
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.requestvalidation.RequestValidation
import ord.pumped.usecase.user.rest.request.validateUserRequest

/**
 * @author=henry
 */
fun Application.configureAkkurate() {
    install(Akkurate)


}