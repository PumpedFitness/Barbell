package app.pumped.api.responses.auth

import kotlinx.serialization.Serializable

/**
 * @author Devin Fritz
 */
@Serializable
data class LoginResponse(val token: String)