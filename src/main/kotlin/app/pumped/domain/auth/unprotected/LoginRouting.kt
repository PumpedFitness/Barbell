package app.pumped.domain.auth.unprotected

import app.pumped.api.requests.auth.LoginRequest
import app.pumped.api.requests.auth.RegisterRequest
import app.pumped.domain.user.User
import app.pumped.domain.user.UserRepository
import app.pumped.plugins.JWTService
import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.loginRouting() {
    post<LoginRequest>("/login") {
        val userRepository by inject<UserRepository>()

        val user = userRepository.getByEmail(it.email)
            ?: return@post call.respondText("Invalid credentials", status = HttpStatusCode.Unauthorized)

        if (!BCrypt.verifyer().verify(it.password.toByteArray(), user.password.toByteArray()).verified) {
            return@post call.respondText("Invalid credentials", status = HttpStatusCode.Unauthorized)
        }

        val jwtService by inject<JWTService>()
        val token = jwtService.createJWTToken(user)
        call.respond("token" to token)
    }

    post<RegisterRequest>("/register") {
        /*
        TODO: David
        - 1 RegisterRequest bauen
        - 2 Nötige sachen prüfen -> email unique und son zeugs (validator)

         */
        val userRepository by inject<UserRepository>()

        val encryptedPassword = BCrypt.withDefaults().hashToString(0,it.password.toCharArray()) //Unsure if cost 0 is correct

        val user = User.new {
            this.email = it.email
            this.password = encryptedPassword
        }

        userRepository.insert(user)

        val jwtService by inject<JWTService>()
        val token = jwtService.createJWTToken(user)
        call.respond("token" to token)
    }


}