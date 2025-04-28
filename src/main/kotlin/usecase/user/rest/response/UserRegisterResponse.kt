package ord.pumped.usecase.user.rest.response

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterResponse(
    val username: String,
    val email: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
