package usecase.user

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import common.IntegrationTestBase
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import ord.pumped.usecase.user.rest.response.UserLoginResponse
import ord.pumped.usecase.user.rest.response.UserRegisterResponse
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserIntegrationTest : IntegrationTestBase() {

    @Test
    @Order(1)
    fun `should fail login if a user is not found`() = testApplication {
        //Arrange
        setupTestApplication()

        //Act
        val response = client.post("/api/v1/user/login") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                {
                    "email": "test@pumped.de",
                    "password": "12345678"
                }
                """.trimIndent()
            )
        }

        //Assert
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(response.bodyAsText(), "User not found")
    }

    @Test
    @Order(2)
    fun `should register a user successfully`() = testApplication {
        // Arrange
        setupTestApplication()

        // Act
        val response = client.post("/api/v1/user/register") {
            contentType(ContentType.Application.Json)
            setBody(
                """
            {
                "email": "test@pumped.de",
                "password": "12345678",
                "username": "testuser"
            }
            """.trimIndent()
            )
        }

        // Assert
        assertEquals(HttpStatusCode.Created, response.status)
        val responseBody = Json.decodeFromString<UserRegisterResponse>(
            response.bodyAsText()
        )
        assertEquals("testuser", responseBody.username)
        assertEquals("test@pumped.de", responseBody.email)
        assertNotNull(responseBody.createdAt)
        assertNotNull(responseBody.updatedAt)
    }

    @Test
    @Order(3)
    fun `should login a user successfully`() = testApplication {
        //Arrange
        setupTestApplication()

        //Act
        val response = client.post("/api/v1/user/login") {
            contentType(ContentType.Application.Json)
            setBody(
                """
            {
                "email": "test@pumped.de",
                "password": "12345678"
            }
            """.trimIndent()
            )
        }

        //Assert
        assertEquals(HttpStatusCode.OK, response.status)
        val responseBody = Json.decodeFromString<UserLoginResponse>(
            response.bodyAsText()
        )
        assertEquals("test@pumped.de", responseBody.email)
        assertNotNull(responseBody.username)
        assertFalse(responseBody.token.isNullOrEmpty())
    }

    @Test
    @Order(5)
    fun `should reject request with invalid JWT`() = testApplication {
        setupTestApplication()

        val response = client.get("/api/v1/auth/user/profile/me") {
            header(HttpHeaders.Authorization, "Bearer invalid_token_here")
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    @Order(6)
    fun `should show me on valid JWT`() = testApplication {
        environment {
            config = MapApplicationConfig(
                "jwt.secret" to "brgtzu89430eko12pijrtfhgue943ko2wdmjdfnhviui",
                "jwt.issuer" to "https://pumped-fitness.de/",
                "jwt.audience" to "pumped-fitness-treadmill"
            )
        }
        setupTestApplication()

        // 2. Login and get token
        val loginResponse = client.post("/api/v1/user/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"test@pumped.de","password":"12345678"}""")
        }
        val loginReponseBody = Json.decodeFromString<UserLoginResponse>(loginResponse.bodyAsText())
        val token = "Bearer ${loginReponseBody.token}"
        println("Obtained Token: $token")

        // 3. Verify token manually
        try {
            val verifier = JWT.require(Algorithm.HMAC256("brgtzu89430eko12pijrtfhgue943ko2wdmjdfnhviui"))
                .withIssuer("https://pumped-fitness.de/")
                .build()
            val decoded = verifier.verify(token.removePrefix("Bearer "))
            println("Token Valid: ${decoded.id}")
        } catch (e: Exception) {
            println("Token Verification Failed: ${e.message}")
        }

        // 4. Make authenticated request
        val response = client.get("/api/v1/auth/user/profile/me") {
            header(HttpHeaders.Authorization, token)
        }
        println("Response Status: ${response.status}")
        println("Response Body: ${response.bodyAsText()}")

        assertEquals(HttpStatusCode.OK, response.status)
    }

}