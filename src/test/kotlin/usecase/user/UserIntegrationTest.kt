package usecase.user

import common.IntegrationTestBase
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UserIntegrationTest : IntegrationTestBase() {

    @Test
    fun `should register a user successfully`() = testApplication {
        setupTestApplication()

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

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should login a user successfully`() = testApplication {
        val response = client.post("/api/v1/user/login") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                {
                    "email": "test@pumped.de",
                    "password": "12345678",
                }
                """.trimIndent()
            )
        }
        val responseBody = response.body<String>()
        print(responseBody)
        assertEquals(HttpStatusCode.OK, response.status)
    }
}