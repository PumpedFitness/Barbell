package ord.pumped.usecase.user.rest.request

import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.constraints.builders.hasLengthBetween
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
    username.hasLengthBetween(IntRange(3, 32))
    username.isNotBlank()
    password.isNotBlank()
    password.hasLengthBetween(IntRange(8, 255))
    password.isMatching(Regex("^[a-zA-Z0-9!@#\$%^&*()\\-_=+\\[\\]\\\\{}|;:'.,<>/?]*$"))
    email.isNotBlank()
    email.isMatching(Regex("^[\\w\\-]+@([\\w-]+\\.)+[\\w-]{2,}$"))
}


fun UserRegisterRequest.Companion.testRequest(): UserRegisterRequest {
    return UserRegisterRequest(
        email = "test@pumped.de",
        password = "12345678",
        username = "testuser"
    )
}