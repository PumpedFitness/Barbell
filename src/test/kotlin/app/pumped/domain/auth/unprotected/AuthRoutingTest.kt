package app.pumped.domain.auth.unprotected

import app.pumped.api.requests.auth.LoginRequest
import app.pumped.api.requests.auth.RegisterRequest
import app.pumped.domain.user.UserRepository
import app.pumped.domain.user.Users
import app.pumped.module
import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.response.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthRoutingTest {
    @BeforeTest
    fun setup() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(Users)
        }
    }

    @Test
    fun testRegistrationandLogin() =
        testApplication {
            application {
                module(false)
            }

            val client =
                createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }

            val registerRequest =
                RegisterRequest(
                    "email@email.com",
                    "xXuser_|_nameXx",
                    "securepassword",
                    true,
                )

            val registerResponse =
                client.post("/api/v1/auth/register") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        registerRequest,
                    )
                }

            assertEquals(HttpStatusCode.OK, registerResponse.status)

            val userRepository by inject<UserRepository>(UserRepository::class.java)

            val user = userRepository.getByEmail(registerRequest.email)!!

            assertTrue(BCrypt.verifyer().verify(registerRequest.password.toByteArray(), user.password.toByteArray()).verified)

            val loginRequest =
                LoginRequest(
                    "email@email.com",
                    "securepassword",
                    true,
                )

            val loginResponse =
                client.post("/api/v1/auth/login") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        loginRequest,
                    )
                }
            assertEquals(HttpStatusCode.OK, loginResponse.status)
        }
}
