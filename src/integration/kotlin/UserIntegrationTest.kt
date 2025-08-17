import common.IntegrationTestBase
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import ord.pumped.usecase.user.rest.response.UserLoginResponse
import ord.pumped.usecase.user.rest.response.UserRegisterResponse
import org.junit.jupiter.api.*

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserIntegrationTest : IntegrationTestBase() {
    val loginRoute = "/api/v1/user/login"
    val registerRoute = "/api/v1/user/register"

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
        Assertions.assertEquals("test@pumped.de", responseBody.email)
        assertNotNull(responseBody.username)
        Assertions.assertFalse(responseBody.token.isNullOrEmpty())
    }

    @Test
    @Order(5)
    fun `should reject request with invalid JWT`() = testApplication {
        setupTestApplication()

        val response = client.get("/api/v1/auth/user/profile/me") {
            header(HttpHeaders.Authorization, "Bearer invalid_token_here")
        }

        Assertions.assertEquals(HttpStatusCode.Companion.Unauthorized, response.status)
    }

//    @Test
//    @Order(6)
//    fun `should show me on valid JWT`() = testApplication {
//        environment {
//            config = MapApplicationConfig(
//                "jwt.secret" to "brgtzu89430eko12pijrtfhgue943ko2wdmjdfnhviui",
//                "jwt.issuer" to "https://pumped-fitness.de/",
//                "jwt.audience" to "pumped-fitness-treadmill"
//            )
//        }
//        setupTestApplication()
//
//        // 2. Login and get token
//        val loginResponse = client.post(loginRoute) {
//            contentType(ContentType.Application.Json)
//            setBody("""{"email":"test@pumped.de","password":"12345678"}""")
//        }
//        val loginReponseBody = Json.Default.decodeFromString<UserLoginResponse>(loginResponse.bodyAsText())
//        val token = "Bearer ${loginReponseBody.token}"
//        println("Obtained Token: $token")
//
//        // 3. Verify token manually
//        try {
//            val verifier = JWT.require(Algorithm.HMAC256("brgtzu89430eko12pijrtfhgue943ko2wdmjdfnhviui"))
//                .withIssuer("https://pumped-fitness.de/")
//                .build()
//            val decoded = verifier.verify(token.removePrefix("Bearer "))
//            println("Token Valid: ${decoded.id}")
//        } catch (e: Exception) {
//            println("Token Verification Failed: ${e.message}")
//        }
//
//        // 4. Make authenticated request
//        val response = client.get("/api/v1/auth/user/profile/me") {
//            header(HttpHeaders.Authorization, token)
//        }
//        println("Response Status: ${response.status}")
//        println("Response Body: ${response.bodyAsText()}")
//
//        Assertions.assertEquals(HttpStatusCode.Companion.OK, response.status)
//    }

}