package ord.pumped.usecase.user.rest.request

import io.ktor.server.plugins.requestvalidation.*
import io.validation.APIRequest

data class UserRegisterRequest(
    val username: String,
    val password: String,
    val email: String

) : APIRequest {

    override fun validate(): ValidationResult {
        //TODO
        return ValidationResult.Valid
    }

}
