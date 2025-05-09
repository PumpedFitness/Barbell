package ord.pumped.common.security.service

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.server.application.Application

interface IJWTService {
    fun verifier(application: Application): JWTVerifier

    fun verifyOrNull(jwtToken: String, application: Application): DecodedJWT?
}