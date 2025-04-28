package ord.pumped.common.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import ord.pumped.io.env.EnvVariables
import ord.pumped.io.env.env
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

class SecurityServiceAdapter: ISecurityService {

    override fun createJWTToken(application: Application, userID: UUID): String {
        return with(application) {
            val jwtAudience = env[EnvVariables.BB_JWT_AUDIENCE]
            val jwtSecret = env[EnvVariables.BB_JWT_SECRET]
            val jwtDomain = env[EnvVariables.BB_JWT_DOMAIN]

            val expiresAt = Clock.System.now().plus(env[EnvVariables.BB_JWT_EXPIRY].toInt().seconds).toJavaInstant()

            JWT
                .create()
                .withIssuer(jwtDomain)
                .withAudience(jwtAudience)
                .withClaim("user_id", userID.toString())
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC256(jwtSecret))
        }
    }
}

val securityModule = module {
    singleOf(::SecurityServiceAdapter) { bind<ISecurityService>() }
}