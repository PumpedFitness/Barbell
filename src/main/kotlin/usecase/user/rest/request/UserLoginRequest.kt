package ord.pumped.usecase.user.rest.request

import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.constraints.builders.isMatching
import dev.nesk.akkurate.constraints.builders.isNotBlank
import kotlinx.serialization.Serializable
import ord.pumped.usecase.user.rest.request.validation.accessors.email
import ord.pumped.usecase.user.rest.request.validation.accessors.password

@Serializable
@Validate
data class UserLoginRequest(
    val email: String,
    val password: String
)

val validateUserLoginRequest = Validator<UserLoginRequest> {
    email.isNotBlank()
    password.isNotBlank()
    email.isMatching(Regex("^[\\w\\-]+@([\\w-]+\\.)+[\\w-]{2,}$"))
}
