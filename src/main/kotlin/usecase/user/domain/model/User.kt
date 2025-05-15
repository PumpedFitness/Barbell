package ord.pumped.usecase.user.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.*

data class User(
    val id: UUID?,
    var username: String,
    var password: String,
    var email: String,
    val description: String? = "",
    val profilePicture: String? = "",
    val createdAt: Instant = Clock.System.now(),
    var updatedAt: Instant = Clock.System.now()
) {
    companion object
}

fun User.Companion.validTestData(): User {
    return User(
        id = UUID.fromString("8688cded-3a55-4a09-9235-6c703388c0e9"),
        username = "username",
        password = "password",
        email = "email@email",
        description = "description",
        profilePicture = "Profile Picture",
        createdAt = Instant.parse("2024-03-08T10:00:00Z"),
        updatedAt = Instant.parse("2024-03-08T10:00:00Z")
    )
}
