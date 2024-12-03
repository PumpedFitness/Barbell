package app.pumped.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        //https://ktor.io/docs/server-request-validation.html#validation-function
    }
}