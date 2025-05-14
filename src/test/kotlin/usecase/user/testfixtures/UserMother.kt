package usecase.user.testfixtures

import kotlinx.datetime.Instant
import ord.pumped.usecase.user.domain.model.User
import java.util.*

class UserMother {
    companion object {
        fun createValidUser(): User {
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
    }
}