package app.pumped.plugins

import app.pumped.api.requests.auth.LoginRequest
import app.pumped.util.isEmailValid
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        //https://ktor.io/docs/server-request-validation.html#validation-function

        validate<LoginRequest> { obj ->
            if (!isEmailValid(obj.email)) {
                ValidationResult.Invalid("Email is not valid!")
            }
            ValidationResult.Valid
        }
    }
}