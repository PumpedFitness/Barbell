package ord.pumped.usecase.user.rest.request

import kotlinx.serialization.Serializable

@Serializable
data class UserDeleteUserRequest(
    val password: String
)

