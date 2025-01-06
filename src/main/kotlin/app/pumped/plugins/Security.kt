package app.pumped.plugins

import app.pumped.data.context.RouteContext
import app.pumped.domain.user.User
import app.pumped.io.env.EnvVariables
import app.pumped.util.Service
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.time.Instant
import java.time.temporal.TemporalUnit
import java.util.concurrent.TimeUnit

fun Application.configureSecurity() {
    // Please read the jwt property from the config file if you are using EngineMain
    val jwtAudience = "jwt"
    val jwtDomain = "https://pumped.fitness"
    val jwtRealm = "pumped"
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

class JWTService: Service {

    fun createJWTToken(user: User): String {
        return JWT.create()
            .withClaim("user_id", user.id.value.toString())
            .withExpiresAt(Instant.now().plusSeconds(globalEnv[EnvVariables.BB_JWT_EXPIRES].toLong()))
            .sign(Algorithm.HMAC256(globalEnv[EnvVariables.BB_JWT_SECRET]))
    }
}
