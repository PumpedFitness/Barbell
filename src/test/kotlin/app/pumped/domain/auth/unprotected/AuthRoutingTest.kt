package app.pumped.domain.auth.unprotected

import app.pumped.api.requests.auth.LoginRequest
import app.pumped.api.requests.auth.RegisterRequest
import app.pumped.api.responses.auth.LoginResponse
import app.pumped.api.responses.auth.RegisterResponse
import app.pumped.domain.user.Users
import app.pumped.module
import app.pumped.seeder.seedUser
import app.pumped.util.httpClient
import app.pumped.util.testClient
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.test.dispatcher.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class AuthRoutingTest {
    @BeforeTest
    fun setup() {
        val app = TestApplication {
            application { module(false) }
        }
        httpClient = app.client.config {
            install(ContentNegotiation) {
                json()
            }
        }

        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(Users)
        }
    }

    @Test
    fun testRegistration() = testSuspend {
            val registerRequest =
                RegisterRequest(
                    "email@email.com",
                    "xXuser_|_nameXx",
                    "securepassword",
                    true,
                )

            val registerResponse = testClient.requestWithBodyAndReturn<RegisterResponse>("/api/v1/auth/register", registerRequest , HttpMethod.Post)

            assertNotNull(registerResponse)
        }

    @Test
    fun testLogin() = testSuspend {
        seedUser()

        val loginRequest =
            LoginRequest(
                "pumped@app.com",
                "12345678",
                true,
            )

        val loginResponse = testClient.requestWithBodyAndReturn<LoginResponse>("/api/v1/auth/login", loginRequest, HttpMethod.Post)
        assertNotNull(loginResponse)
    }
}
