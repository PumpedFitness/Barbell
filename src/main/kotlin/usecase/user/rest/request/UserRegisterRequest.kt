package ord.pumped.usecase.user.rest.request

import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.constraints.builders.isMatching
import dev.nesk.akkurate.constraints.builders.isNotBlank
import kotlinx.serialization.Serializable
import ord.pumped.usecase.user.rest.request.validation.accessors.email
import ord.pumped.usecase.user.rest.request.validation.accessors.password
import ord.pumped.usecase.user.rest.request.validation.accessors.username

@Validate
@Serializable
data class UserRegisterRequest(
    val username: String,
    val password: String,
    val email: String

)

val validateUserRegisterRequest = Validator<UserRegisterRequest> {
    username.isNotBlank()
    password.isNotBlank()
    email.isNotBlank()
    email.isMatching(Regex("^[\\w\\-]+@([\\w-]+\\.)+[\\w-]{2,}$"))
}

