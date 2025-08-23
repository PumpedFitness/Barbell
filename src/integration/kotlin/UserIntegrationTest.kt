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

@TestClassOrder(ClassOrderer.OrderAnnotation::class)
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

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
    inner class HappyPaths {
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

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
    inner class NonHappyPaths {
        @Test
        @Order(1)
        fun `should reject registering with duplicate email`() = testApplication {
            // Arrange
            setupTestApplication()
            val email = "dup@pumped.de"
            val first = client.post(registerRoute) {
                contentType(ContentType.Application.Json)
                setBody(
                    """
                    {"email":"$email","password":"12345678","username":"dupUser"}
                    """.trimIndent()
                )
            }
            // Act
            val second = client.post(registerRoute) {
                contentType(ContentType.Application.Json)
                setBody(
                    """
                    {"email":"$email","password":"12345678","username":"dupUser"}
                    """.trimIndent()
                )
            }

            // Assert
            Assertions.assertEquals(HttpStatusCode.Created, first.status)
            Assertions.assertEquals(HttpStatusCode.Conflict, second.status)
            Assertions.assertEquals("Email is already used", second.bodyAsText())
        }

        @Test
        @Order(2)
        fun `should fail login with wrong password`() = testApplication {
            // Arrange
            setupTestApplication()
            val email = "wrongpass@pumped.de"
            val reg = client.post(registerRoute) {
                contentType(ContentType.Application.Json)
                setBody(
                    """
                    {"email":"$email","password":"correctPW1","username":"wrongpass"}
                    """.trimIndent()
                )
            }
            Assertions.assertEquals(HttpStatusCode.Created, reg.status)

            // Act
            val login = client.post(loginRoute) {
                contentType(ContentType.Application.Json)
                setBody(
                    """
                    {"email":"$email","password":"totallyWrong"}
                    """.trimIndent()
                )
            }

            // Assert
            Assertions.assertEquals(HttpStatusCode.BadRequest, login.status)
            Assertions.assertEquals("Invalid Password", login.bodyAsText())
        }

        @Test
        @Order(3)
        fun `should fail updating password with wrong old password`() = testApplication {
            // Arrange
            setupTestApplication()
            val email = "pwupdate@pumped.de"
            val reg = client.post(registerRoute) {
                contentType(ContentType.Application.Json)
                setBody(
                    """
                    {"email":"$email","password":"oldPass123","username":"pwupdate"}
                    """.trimIndent()
                )
            }
            Assertions.assertEquals(HttpStatusCode.Created, reg.status)

            val login = client.post(loginRoute) {
                contentType(ContentType.Application.Json)
                setBody(
                    """
                    {"email":"$email","password":"oldPass123"}
                    """.trimIndent()
                )
            }
            Assertions.assertEquals(HttpStatusCode.OK, login.status)
            val loginBody = Json.Default.decodeFromString<UserLoginResponse>(login.bodyAsText())
            val token = loginBody.token ?: ""

            // Act
            val update = client.put(passwordUpdateRoute) {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(
                    """
                    {"oldPassword":"WRONG_OLD","newPassword":"newPass999"}
                    """.trimIndent()
                )
            }

            // Assert
            Assertions.assertEquals(HttpStatusCode.BadRequest, update.status)
            Assertions.assertEquals("Invalid Password", update.bodyAsText())
        }

        @Test
        @Order(4)
        fun `should fail deleting user with wrong password`() = testApplication {
            // Arrange
            setupTestApplication()
            val email = "del@pumped.de"
            val reg = client.post(registerRoute) {
                contentType(ContentType.Application.Json)
                setBody(
                    """
                    {"email":"$email","password":"toDelete123","username":"deluser"}
                    """.trimIndent()
                )
            }
            Assertions.assertEquals(HttpStatusCode.Created, reg.status)

            val login = client.post(loginRoute) {
                contentType(ContentType.Application.Json)
                setBody(
                    """
                    {"email":"$email","password":"toDelete123"}
                    """.trimIndent()
                )
            }
            Assertions.assertEquals(HttpStatusCode.OK, login.status)
            val token = Json.Default.decodeFromString<UserLoginResponse>(login.bodyAsText()).token ?: ""

            // Act
            val delete = client.delete(userDeletionRoute) {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(
                    """
                    {"password":"WRONG_PASSWORD"}
                    """.trimIndent()
                )
            }

            // Assert
            Assertions.assertEquals(HttpStatusCode.BadRequest, delete.status)
            Assertions.assertEquals("Invalid Password", delete.bodyAsText())
        }

        @Test
        @Order(5)
        fun `should not allow using token after logout`() = testApplication {
            // Arrange
            setupTestApplication()
            val email = "logout@pumped.de"
            val reg = client.post(registerRoute) {
                contentType(ContentType.Application.Json)
                setBody(
                    """
                    {"email":"$email","password":"logoutMe","username":"logoutuser"}
                    """.trimIndent()
                )
            }
            Assertions.assertEquals(HttpStatusCode.Created, reg.status)

            val login = client.post(loginRoute) {
                contentType(ContentType.Application.Json)
                setBody(
                    """
                    {"email":"$email","password":"logoutMe"}
                    """.trimIndent()
                )
            }
            Assertions.assertEquals(HttpStatusCode.OK, login.status)

            val token = Json.Default.decodeFromString<UserLoginResponse>(login.bodyAsText()).token ?: ""
            val logout = client.delete(logoutRoute) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            Assertions.assertEquals(HttpStatusCode.OK, logout.status)

            // Act
            val me = client.get(meRoute) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            // Assert
            Assertions.assertEquals(HttpStatusCode.Unauthorized, me.status)
        }
    }

}