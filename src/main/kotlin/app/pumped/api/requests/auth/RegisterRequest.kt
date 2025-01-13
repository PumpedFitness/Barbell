package app.pumped.api.requests.auth

import app.pumped.api.APIRequest
import app.pumped.domain.user.UserRepository
import app.pumped.util.isEmailValid
import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.java.KoinJavaComponent.inject

/**
 * @author David Damm
 */
@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    @SerialName("remember_me") val rememberMe: Boolean,
) : APIRequest {
    override fun validate(): ValidationResult {
        val userRepository by inject<UserRepository>(UserRepository::class.java)

        if (!isEmailValid(email)) {
            return ValidationResult.Invalid("Email is not valid!")
        }

        if (userRepository.getByEmail(email) != null) {
            return ValidationResult.Invalid("This email is already taken!")
        }

        return ValidationResult.Valid
    }
}
