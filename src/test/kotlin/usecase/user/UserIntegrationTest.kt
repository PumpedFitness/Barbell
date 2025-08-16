package usecase.user

import common.IntegrationTestBase
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import ord.pumped.usecase.user.rest.response.UserLoginResponse
import ord.pumped.usecase.user.rest.response.UserMeResponse
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
        //Arrange
        lateinit var jwtToken: String
        setupTestApplication()
        val preResponse = client.post("/api/v1/user/login") {
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
        assertEquals(HttpStatusCode.OK, preResponse.status)
        val preResponseBody = Json.decodeFromString<UserLoginResponse>(
            preResponse.bodyAsText()
        )
        assertFalse(preResponseBody.token.isNullOrEmpty())
        jwtToken = preResponseBody.token ?: ""

        //Act
        val response = client.get("/api/v1/auth/user/profile/me") {
            header(HttpHeaders.Authorization, "Bearer $jwtToken")
        }

        //Assert
        assertEquals(HttpStatusCode.OK, response.status)
        val responseBody = Json.decodeFromString<UserMeResponse>(
            response.bodyAsText()
        )
        assertEquals("testuser", responseBody.username)
        assertEquals("test@pumped.de", responseBody.email)
        assertNotNull(responseBody.createdAt)
        assertNotNull(responseBody.updatedAt)
        assertEquals("", responseBody.description)
        assertEquals("", responseBody.profilePicture)
    }

}