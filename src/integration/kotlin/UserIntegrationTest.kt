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

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserIntegrationTest : IntegrationTestBase() {
    val loginRoute = "/api/v1/user/login"
    val registerRoute = "/api/v1/user/register"
    val meRoute = "/api/v1/auth/user/profile/me"
    val logoutRoute = "/api/v1/auth/user/logout"
    val passwordUpdateRoute = "/api/v1/auth/user/update/password"
    val profileUpdateRoute = "/api/v1/auth/user/profile/update"
    val userDeletionRoute = "/api/v1/auth/user/delete"

    companion object {
        private lateinit var sharedJwtToken: String
    }

    @Test
    @Order(1)
    fun `should fail login if a user is not found`() = testApplication {
        //Arrange
        setupTestApplication()

        //Act
        val response = client.post(loginRoute) {
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
        Assertions.assertEquals(HttpStatusCode.Companion.BadRequest, response.status)
        Assertions.assertEquals(response.bodyAsText(), "User not found")
    }

    @Test
    @Order(2)
    fun `should register a user successfully`() = testApplication {
        // Arrange
        setupTestApplication()

        // Act
        val response = client.post(registerRoute) {
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
        Assertions.assertEquals(HttpStatusCode.Companion.Created, response.status)
        val responseBody = Json.Default.decodeFromString<UserRegisterResponse>(
            response.bodyAsText()
        )
        Assertions.assertEquals("testuser", responseBody.username)
        Assertions.assertEquals("test@pumped.de", responseBody.email)
        assertNotNull(responseBody.createdAt)
        assertNotNull(responseBody.updatedAt)
    }

    @Test
    @Order(3)
    fun `should login a user successfully`() = testApplication {
        //Arrange
        setupTestApplication()

        //Act
        val response = client.post(loginRoute) {
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
        Assertions.assertEquals(HttpStatusCode.Companion.OK, response.status)
        val responseBody = Json.Default.decodeFromString<UserLoginResponse>(
            response.bodyAsText()
        )
        sharedJwtToken = responseBody.token ?: "empty token"
        Assertions.assertEquals("test@pumped.de", responseBody.email)
        assertNotNull(responseBody.username)
        Assertions.assertFalse(responseBody.token.isNullOrEmpty())
    }

    @Test
    @Order(4)
    fun `should reject request with invalid JWT`() = testApplication {
        //Arrange
        setupTestApplication()

        // Act
        val response = client.get(meRoute) {
            header(HttpHeaders.Authorization, "Bearer invalid_token_here")
        }

        // Assert
        Assertions.assertEquals(HttpStatusCode.Companion.Unauthorized, response.status)
    }

    @Test
    @Order(5)
    fun `should logout a user successfully`() = testApplication {
        // Arrange
        setupTestApplication()

        // Act
        val response = client.delete(logoutRoute) {
            header(HttpHeaders.Authorization, "Bearer $sharedJwtToken")
        }

        Assertions.assertEquals(HttpStatusCode.Companion.OK, response.status)
    }

    @Test
    @Order(6)
    fun `should show me on valid JWT`() = testApplication {
        // Arrange
        setupTestApplication()
        val loginResponse = client.post(loginRoute) {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"test@pumped.de","password":"12345678"}""")
        }
        val loginReponseBody = Json.Default.decodeFromString<UserLoginResponse>(loginResponse.bodyAsText())
        sharedJwtToken = loginReponseBody.token ?: "empty token"

        // Act
        val response = client.get(meRoute) {
            header(HttpHeaders.Authorization, "Bearer $sharedJwtToken")
        }
        val responseBody = Json.Default.decodeFromString<UserMeResponse>(response.bodyAsText())

        // Assert
        Assertions.assertEquals(HttpStatusCode.Companion.OK, response.status)
        Assertions.assertEquals("test@pumped.de", responseBody.email)
        Assertions.assertEquals("testuser", responseBody.username)
        Assertions.assertNotNull(responseBody.description)
        Assertions.assertNotNull(responseBody.profilePicture)
        Assertions.assertNotNull(responseBody.createdAt)
        Assertions.assertNotNull(responseBody.updatedAt)
        Assertions.assertNotNull(responseBody.id)
    }

    @Test
    @Order(7)
    fun `should update password on existing user`() = testApplication {
        // Arrange
        setupTestApplication()

        // Act
        val response = client.put(passwordUpdateRoute) {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $sharedJwtToken")
            setBody("""{"oldPassword":"12345678","newPassword":"newPassword123"}""")
        }

        //Assert
        Assertions.assertEquals(HttpStatusCode.Companion.OK, response.status)
    }

    @Test
    @Order(8)
    fun `should update profile on existing user`() = testApplication {
        // Arrange
        setupTestApplication()

        // Act
        val response = client.put(profileUpdateRoute) {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $sharedJwtToken")
            setBody("""{"username":"Change","description":"Description","profilePicture":"ProfilePicture"}""")
        }
        val responseBody = Json.Default.decodeFromString<UserMeResponse>(response.bodyAsText())

        // Assert
        Assertions.assertEquals(HttpStatusCode.Companion.OK, response.status)
        Assertions.assertEquals("test@pumped.de", responseBody.email)
        Assertions.assertEquals("Change", responseBody.username)
        Assertions.assertEquals("Description", responseBody.description)
        Assertions.assertEquals("ProfilePicture", responseBody.profilePicture)
        Assertions.assertNotNull(responseBody.createdAt)
        Assertions.assertNotNull(responseBody.updatedAt)
        Assertions.assertNotNull(responseBody.id)
    }

    @Test
    @Order(9)
    fun `should delete existing user`() = testApplication {
        // Arrange
        setupTestApplication()

        // Act
        val response = client.delete(userDeletionRoute) {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $sharedJwtToken")
            setBody("""{"password":"newPassword123"}""")
        }

        // Assert
        Assertions.assertEquals(HttpStatusCode.Companion.OK, response.status)
    }
}