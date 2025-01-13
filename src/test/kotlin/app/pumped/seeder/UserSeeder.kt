package app.pumped.seeder

import app.pumped.api.requests.auth.RegisterRequest
import app.pumped.api.responses.auth.RegisterResponse
import app.pumped.util.testClient
import io.ktor.http.*

suspend fun seedUser(email: String = "pumped@app.com", password: String = "12345678") {
    val registerRequest =
        RegisterRequest(
            email,
            "pumped",
            password,
            true,
        )

    testClient.requestWithBodyAndReturn<RegisterResponse>("/api/v1/auth/register", registerRequest , HttpMethod.Post)
}