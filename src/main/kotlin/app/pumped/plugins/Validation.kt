package app.pumped.plugins

import app.pumped.api.APIRequest
import app.pumped.api.requests.auth.LoginRequest
import app.pumped.util.isEmailValid
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        //https://ktor.io/docs/server-request-validation.html#validation-function
        validate<LoginRequest>()
    }
}

inline fun <reified T: APIRequest> RequestValidationConfig.validate() {
    validate<T> { validatingObject -> validatingObject.validate() }
}
