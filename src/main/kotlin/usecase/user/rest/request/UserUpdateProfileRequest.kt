package ord.pumped.usecase.user.rest.request

import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.constraints.builders.hasLengthLowerThanOrEqualTo
import kotlinx.serialization.Serializable
import ord.pumped.usecase.user.rest.request.validation.accessors.description
import ord.pumped.usecase.user.rest.request.validation.accessors.profilePicture

@Serializable
@Validate
data class UserUpdateProfileRequest(
    val username: String?,
    val description: String?,
    val profilePicture: String?
)

val validateUpdateProfileRequest = Validator<UserUpdateProfileRequest> {
    description.hasLengthLowerThanOrEqualTo(500)
    profilePicture.hasLengthLowerThanOrEqualTo(250)
}

fun UserUpdateProfileRequest.Companion.testRequest(): UserUpdateProfileRequest {
    return UserUpdateProfileRequest(
        username = "Change",
        description = "Description",
        profilePicture = "ProfilePicture"
    )
}