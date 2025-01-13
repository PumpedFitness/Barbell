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
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

fun Route.authRouting() {
    post<LoginRequest>("/login") {
        val userRepository by inject<UserRepository>()

        val user =
            userRepository.getByEmail(it.email)
                ?: return@post call.respondText("Invalid credentials", status = HttpStatusCode.Unauthorized)

        if (!BCrypt.verifyer().verify(it.password.toByteArray(), user.password.toByteArray()).verified) {
            return@post call.respondText("Invalid credentials", status = HttpStatusCode.Unauthorized)
        }

        val jwtService by inject<JWTService>()
        val token = jwtService.createJWTToken(user)
        call.respond("token" to token)
    }

    post<RegisterRequest>("/register") {
        val hashedPassword = BCrypt.withDefaults().hashToString(12, it.password.toCharArray())
        val jwtService by inject<JWTService>()

        transaction {
            val user =
                User.new {
                    this.username = it.username
                    this.email = it.email
                    this.password = hashedPassword
                }
            val token = jwtService.createJWTToken(user)
            runBlocking {
                call.respond("token" to token)
            }
        }
    }
}
