package ord.pumped.configuration

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import ord.pumped.common.exceptions.UnauthorizedException
import ord.pumped.io.env.EnvVariables
import ord.pumped.io.env.env
import java.util.*

fun Application.configureSecurity() {
    val jwtDomain = env[EnvVariables.BB_JWT_DOMAIN]
    val jwtRealm = env[EnvVariables.BB_JWT_REALM]
    val jwtAudience = env[EnvVariables.BB_JWT_AUDIENCE]
    val jwtSecret = env[EnvVariables.BB_JWT_SECRET]

    authentication {
        jwt("jwt") {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
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
    val uuidClaim = principal<JWTPrincipal>()?.payload?.getClaim("user_id") ?: throw UnauthorizedException()
    return UUID.fromString(uuidClaim.asString())
}


