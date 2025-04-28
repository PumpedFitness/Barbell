package ord.pumped.usecase.user.rest.response

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginResponse(
    val email: String,
    val username: String,
    var token: String? = "",
)
