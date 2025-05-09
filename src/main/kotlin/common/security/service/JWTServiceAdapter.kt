package ord.pumped.common.security.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.server.application.Application
import ord.pumped.configuration.secrets
import ord.pumped.io.env.EnvVariables

class JWTServiceAdapter: IJWTService {

    override fun verifier(application: Application): JWTVerifier {
        return with (application) {
            val jwtDomain = secrets[EnvVariables.BB_JWT_DOMAIN]
            val jwtAudience = secrets[EnvVariables.BB_JWT_AUDIENCE]
            val jwtSecret = secrets[EnvVariables.BB_JWT_SECRET]

            JWT
                .require(Algorithm.HMAC256(jwtSecret))
                .withAudience(jwtAudience)
                .withIssuer(jwtDomain)
                .build()
        }
    }

    override fun verifyOrNull(jwtToken: String, application: Application): DecodedJWT? {
        return try {
            verifier(application).verify(jwtToken)
        } catch (_: Exception) {
            null
        }
    }
}