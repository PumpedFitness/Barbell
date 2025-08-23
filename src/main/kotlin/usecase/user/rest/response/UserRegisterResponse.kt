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

fun UserRegisterResponse.Companion.testResponse(): UserRegisterResponse {
    return UserRegisterResponse(
        username = "testuser",
        email = "test@pumped.de",
        createdAt = Instant.fromEpochMilliseconds(1000),
        updatedAt = Instant.fromEpochMilliseconds(1000)
    )
}