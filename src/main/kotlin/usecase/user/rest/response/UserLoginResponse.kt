package ord.pumped.usecase.user.rest.response

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginResponse(
    val email: String,
    val username: String,
    var token: String? = "",
)

fun UserLoginResponse.Companion.testResponse(): UserLoginResponse {
    return UserLoginResponse(
        email = "test@pumped.de",
        username = "testuser",
    )
}