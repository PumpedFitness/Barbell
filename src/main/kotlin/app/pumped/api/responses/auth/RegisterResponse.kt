package app.pumped.api.responses.auth

import app.pumped.domain.user.generated.UserModelDTO
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(val token: String, val user: UserModelDTO)