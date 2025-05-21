package usecase.user

import common.IntegrationTestBase
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UserIntegrationTest : IntegrationTestBase() {

    @Test
    fun `should register a user successfully`() = testApplication {
        setupTestApplication()

        val response = client.post("/register") {
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

        assertEquals(HttpStatusCode.Created, response.status)
    }
}