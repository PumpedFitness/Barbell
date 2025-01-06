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
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureSecurity() {
    // Please read the jwt property from the config file if you are using EngineMain
    val jwtAudience = "jwt-audience"
    val jwtDomain = "https://pumped.fitness"
    val jwtRealm = "pumped"
    val jwtSecret = env[EnvVariables.BB_JWT_SECRET]
    authentication {
        jwt {
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
            .withClaim("","").sign(Algorithm.HMAC256(globalEnv[EnvVariables.BB_JWT_SECRET]))
    }
}
