package app.pumped.domain.auth.unprotected

import app.pumped.api.requests.auth.RegisterRequest
import app.pumped.domain.user.Users
import app.pumped.module
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthRoutingTest {
    @BeforeTest
    fun setup() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(Users)
        }
    }

    @Test
    fun testRootEndpoint() =
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

            val response =
                client.post("/api/v1/auth/register") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        RegisterRequest(
                            "email@email.com",
                            "securepassword",
                            true,
                        ),
                    )
                }

            assertEquals(HttpStatusCode.OK, response.status)
        }
}
