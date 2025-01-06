package app.pumped.api.requests.auth

import kotlinx.serialization.Serializable

/**
 * @author Devin Fritz
 */
@Serializable
data class LoginRequest(val email: String, val password: String, val rememberMe: Boolean)