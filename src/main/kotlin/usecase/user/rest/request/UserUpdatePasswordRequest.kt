package ord.pumped.usecase.user.rest.request

import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.constraints.builders.hasLengthBetween
import dev.nesk.akkurate.constraints.builders.isMatching
import kotlinx.serialization.Serializable
import ord.pumped.usecase.user.rest.request.validation.accessors.newPassword

@Serializable
@Validate
data class UserUpdatePasswordRequest(
    val oldPassword: String,
    val newPassword: String,
)

val validateUserUpdatePasswordRequest = Validator<UserUpdatePasswordRequest> {
    newPassword.hasLengthBetween(IntRange(8, 255))
    newPassword.isMatching(Regex("^[a-zA-Z0-9!@#\$%^&*()\\-_=+\\[\\]\\\\{}|;:'.,<>/?]*$"))
}

fun UserUpdatePasswordRequest.Companion.testRequest(): UserUpdatePasswordRequest {
    return UserUpdatePasswordRequest(
        oldPassword = "12345678",
        newPassword = "newPassword123"
    )
}