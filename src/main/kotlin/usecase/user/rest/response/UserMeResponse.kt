package ord.pumped.usecase.user.rest.response

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserMeResponse(
    val id: String,
    val username: String,
    val email: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val description: String,
    val profilePicture: String
)

fun UserMeResponse.Companion.testResponse(): UserMeResponse {
    return UserMeResponse(
        id = "some id",
        username = "testuser",
        email = "test@pumped.de",
        createdAt = Instant.fromEpochMilliseconds(123),
        updatedAt = Instant.fromEpochMilliseconds(123),
        description = "this is a description",
        profilePicture = "this is a picture"
    )
}