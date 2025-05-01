package ord.pumped.configuration

import dev.nesk.akkurate.ktor.server.registerValidator
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import ord.pumped.common.APIException
import ord.pumped.usecase.user.rest.request.validateUpdateProfileRequest
import ord.pumped.usecase.user.rest.request.validateUserLoginRequest
import ord.pumped.usecase.user.rest.request.validateUserRegisterRequest
import ord.pumped.usecase.user.rest.request.validateUserUpdatePasswordRequest

fun Application.configureRouting() {
    install(RequestValidation) {
        registerValidator(validateUserRegisterRequest)
        registerValidator(validateUserLoginRequest)
        registerValidator(validateUpdateProfileRequest)
        registerValidator(validateUserUpdatePasswordRequest)
    }
    install(Resources)

    install(StatusPages) {
        exception<APIException> { call, cause ->
            cause.handle(call)
        }
    }
}
