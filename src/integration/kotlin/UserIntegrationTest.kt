import common.IntegrationTestBase
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import ord.pumped.usecase.user.rest.request.*
import ord.pumped.usecase.user.rest.response.UserLoginResponse
import ord.pumped.usecase.user.rest.response.UserMeResponse
import ord.pumped.usecase.user.rest.response.UserRegisterResponse
import ord.pumped.usecase.user.rest.response.testResponse
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
    val loginRequest = Json.encodeToString(UserLoginRequest.testRequest()).trimIndent()
    val registerRequest = Json.encodeToString(UserRegisterRequest.testRequest()).trimIndent()
    val userUpdatePasswordRequest = Json.encodeToString(UserUpdatePasswordRequest.testRequest()).trimIndent()
    val userUpdateProfileRequest = Json.encodeToString(UserUpdateProfileRequest.testRequest()).trimIndent()

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
                setBody(loginRequest)
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
            val expectedResponse = UserRegisterResponse.testResponse()

            // Act
            val response = client.post(registerRoute) {
                contentType(ContentType.Application.Json)
                setBody(registerRequest)
            }

            // Assert
            Assertions.assertEquals(HttpStatusCode.Companion.Created, response.status)
            val responseBody = Json.decodeFromString<UserRegisterResponse>(
                response.bodyAsText()
            )
            Assertions.assertEquals(expectedResponse.username, responseBody.username)
            Assertions.assertEquals(expectedResponse.email, responseBody.email)
            assertNotNull(responseBody.createdAt)
            assertNotNull(responseBody.updatedAt)
        }

        @Test
        @Order(3)
        fun `should login a user successfully`() = testApplication {
            //Arrange
            setupTestApplication()
            val expectedResponse = UserLoginResponse.testResponse()

            //Act
            val response = client.post(loginRoute) {
                contentType(ContentType.Application.Json)
                setBody(loginRequest)
            }

            //Assert
            Assertions.assertEquals(HttpStatusCode.Companion.OK, response.status)
            val responseBody = Json.decodeFromString<UserLoginResponse>(
                response.bodyAsText()
            )
            sharedJwtToken = responseBody.token ?: "empty token"
            Assertions.assertEquals(expectedResponse.email, responseBody.email)
            Assertions.assertEquals(expectedResponse.username, responseBody.username)
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
            val expectedResponse = UserMeResponse.testResponse()
            val loginResponse = client.post(loginRoute) {
                contentType(ContentType.Application.Json)
                setBody(loginRequest)
            }
            Assertions.assertEquals(HttpStatusCode.Companion.OK, loginResponse.status)
            val loginReponseBody = Json.decodeFromString<UserLoginResponse>(loginResponse.bodyAsText())
            sharedJwtToken = loginReponseBody.token ?: "empty token"

            // Act
            val response = client.get(meRoute) {
                header(HttpHeaders.Authorization, "Bearer $sharedJwtToken")
            }
            val responseBody = Json.decodeFromString<UserMeResponse>(response.bodyAsText())

            // Assert
            Assertions.assertEquals(HttpStatusCode.Companion.OK, response.status)
            Assertions.assertEquals(expectedResponse.email, responseBody.email)
            Assertions.assertEquals(expectedResponse.username, responseBody.username)
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
                setBody(userUpdatePasswordRequest)
            }

            //Assert
            Assertions.assertEquals(HttpStatusCode.Companion.OK, response.status)
        }

        @Test
        @Order(8)
        fun `should update profile on existing user`() = testApplication {
            // Arrange
            setupTestApplication()
            val expectedResponse = UserMeResponse.testResponse().copy(
                username = "Change",
                description = "Description",
                profilePicture = "ProfilePicture"
            )

            // Act
            val response = client.put(profileUpdateRoute) {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $sharedJwtToken")
                setBody(userUpdateProfileRequest)
            }
            val responseBody = Json.decodeFromString<UserMeResponse>(response.bodyAsText())

            // Assert
            Assertions.assertEquals(HttpStatusCode.Companion.OK, response.status)
            Assertions.assertEquals(expectedResponse.email, responseBody.email)
            Assertions.assertEquals(expectedResponse.username, responseBody.username)
            Assertions.assertEquals(expectedResponse.description, responseBody.description)
            Assertions.assertEquals(expectedResponse.profilePicture, responseBody.profilePicture)
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
                setBody(Json.encodeToString(UserDeleteUserRequest.testRequest()).trimIndent())
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
            val firstRequest = client.post(registerRoute) {
                contentType(ContentType.Application.Json)
                setBody(registerRequest)
            }

            // Act
            val secondRequest = client.post(registerRoute) {
                contentType(ContentType.Application.Json)
                setBody(registerRequest)
            }

            // Assert
            Assertions.assertEquals(HttpStatusCode.Created, firstRequest.status)
            Assertions.assertEquals(HttpStatusCode.Conflict, secondRequest.status)
            Assertions.assertEquals("Email is already used", secondRequest.bodyAsText())
        }

        @Test
        @Order(2)
        fun `should fail login with wrong password`() = testApplication {
            // Arrange
            setupTestApplication()

            // Act
            val loginBody = Json.encodeToString(
                UserLoginRequest.testRequest().copy(password = "totallyWrong")
            ).trimIndent()
            val loginResponse = client.post(loginRoute) {
                contentType(ContentType.Application.Json)
                setBody(loginBody)
            }

            // Assert
            Assertions.assertEquals(HttpStatusCode.BadRequest, loginResponse.status)
            Assertions.assertEquals("Invalid Password", loginResponse.bodyAsText())
        }

        @Test
        @Order(3)
        fun `should fail updating password with wrong old password`() = testApplication {
            setupTestApplication()
            // Arrange
            val newRegisteredUser =
                Json.encodeToString(UserRegisterRequest.testRequest().copy(email = "differentEmail@user.de"))
                    .trimIndent()
            val newUserLoginRequest =
                Json.encodeToString(UserLoginRequest.testRequest().copy(email = "differentEmail@user.de")).trimIndent()
            val updateRequest = Json.encodeToString(
                UserUpdatePasswordRequest.testRequest().copy(
                    oldPassword = "wrongPassword"
                )
            ).trimIndent()
            val registerResponse = client.post(registerRoute) {
                contentType(ContentType.Application.Json)
                setBody(newRegisteredUser)
            }
            Assertions.assertEquals(HttpStatusCode.Created, registerResponse.status)

            val loginRequest = client.post(loginRoute) {
                contentType(ContentType.Application.Json)
                setBody(newUserLoginRequest)
            }
            Assertions.assertEquals(HttpStatusCode.OK, loginRequest.status)
            sharedJwtToken = Json.decodeFromString<UserLoginResponse>(loginRequest.bodyAsText()).token ?: "empty token"


            // Act
            val updatePasswordRequest = client.put(passwordUpdateRoute) {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $sharedJwtToken")
                setBody(updateRequest)
            }

            // Assert
            Assertions.assertEquals(HttpStatusCode.BadRequest, updatePasswordRequest.status)
            Assertions.assertEquals("Invalid Password", updatePasswordRequest.bodyAsText())
        }

        @Test
        @Order(4)
        fun `should fail deleting user with wrong password`() = testApplication {
            // Arrange
            setupTestApplication()
            val deleteRequest = Json.encodeToString(
                UserDeleteUserRequest.testRequest().copy(password = "WRONG_PASSWORD")
            ).trimIndent()

            // Act

            val deleteResponse = client.delete(userDeletionRoute) {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $sharedJwtToken")
                setBody(deleteRequest)
            }

            // Assert
            Assertions.assertEquals(HttpStatusCode.BadRequest, deleteResponse.status)
            Assertions.assertEquals("Invalid Password", deleteResponse.bodyAsText())
        }

        @Test
        @Order(5)
        fun `should not allow using token after logout`() = testApplication {
            // Arrange
            setupTestApplication()
            val logout = client.delete(logoutRoute) {
                header(HttpHeaders.Authorization, "Bearer $sharedJwtToken")
            }
            Assertions.assertEquals(HttpStatusCode.OK, logout.status)

            // Act
            val me = client.get(meRoute) {
                header(HttpHeaders.Authorization, "Bearer $sharedJwtToken")
            }

            // Assert
            Assertions.assertEquals(HttpStatusCode.Unauthorized, me.status)
        }
    }

}