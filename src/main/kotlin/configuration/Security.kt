package ord.pumped.configuration

import com.auth0.jwt.interfaces.Claim
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import ord.pumped.common.exceptions.UnauthorizedException
import ord.pumped.common.security.domain.mapper.TokenModelMapper
import ord.pumped.common.security.persistance.repository.TokenRepository
import ord.pumped.common.security.service.IJWTService
import ord.pumped.io.env.EnvVariables
import ord.pumped.usecase.user.persistence.repository.IUserRepository
import ord.pumped.usecase.user.persistence.repository.UserRepository
import org.koin.ktor.ext.inject
import java.time.Instant
import java.util.*

fun Application.configureSecurity() {
    val jwtRealm = secrets[EnvVariables.BB_JWT_REALM]
    val jwtAudience = secrets[EnvVariables.BB_JWT_AUDIENCE]

    val jwtService by inject<IJWTService>()
    val userRepository by inject<IUserRepository>()
    val tokenRepository by inject<TokenRepository>()
    val tokenModelMapper by inject<TokenModelMapper>()

    authentication {
        jwt("jwt") {
            realm = jwtRealm
            verifier(
                jwtService.verifier(this@configureSecurity)
            )
            validate { credential ->
                val userID = credential.payload.getClaim("user_id").asString() ?: throw UnauthorizedException()
                val tokenID = credential.payload.getClaim("token_id").asString() ?: throw UnauthorizedException()

                if (userRepository.findByID(UUID.fromString(userID)) == null) {
                    throw UnauthorizedException()
                }

                val token = tokenRepository.findByID(UUID.fromString(tokenID)) ?: throw UnauthorizedException()
                val tokenModel = tokenModelMapper.toDomain(token)

                if (tokenModel.isBlacklisted) {
                    throw UnauthorizedException()
                }

                if (credential.payload.expiresAt.toInstant().isBefore(Instant.now())) {
                    throw UnauthorizedException()
                }

                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}

/**
 * Fetches the current user id from the current jwt session, if provided.
 * Only use if route is inside API gateway
 */
fun ApplicationCall.userID(): UUID {
    val uuidClaim = getAuthClaim("user_id") ?: throw UnauthorizedException()
    return UUID.fromString(uuidClaim.asString())
}

private fun ApplicationCall.principalByCookie(): JWTPrincipal? {
    val jwtService by inject<IJWTService>()
    val cookie = request.cookies[BB_COOKIE] ?: return null
    val payload = jwtService.verifyOrNull(cookie, application) ?: return null
    return JWTPrincipal(payload)
}

private fun ApplicationCall.principalByHeader() = principal<JWTPrincipal>()

private fun ApplicationCall.getAuthClaim(claim: String): Claim? {
    val principal = principalByCookie() ?: principalByHeader() ?: return null
    return principal.payload.getClaim(claim)
}

fun ApplicationCall.tokenID(): UUID {
    val uuidClaim = getAuthClaim("token_id") ?: throw UnauthorizedException()
    return UUID.fromString(uuidClaim.asString())
}


