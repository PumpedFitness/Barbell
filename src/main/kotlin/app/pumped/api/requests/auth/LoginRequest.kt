package app.pumped.api.requests.auth

import app.pumped.api.APIRequest
import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Devin Fritz
 */
@Serializable
data class LoginRequest(val email: String, val password: String, @SerialName("remember_me") val rememberMe: Boolean): APIRequest {

    override fun validate(): ValidationResult {
        return ValidationResult.Valid
    }
}