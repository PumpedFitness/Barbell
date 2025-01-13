package app.pumped.util

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*


lateinit var httpClient: HttpClient

val testClient = TestClient()

class TestClient {
    suspend inline fun <reified T> requestWithBodyAndReturn(
        url: String,
        body: Any,
        method: HttpMethod,
        onError: (error: String, status: HttpStatusCode) -> Unit = { _, _ ->
            run {
            }
        },
    ): T? {
        try {
            val response =
                httpClient.request(url) {
                    this.method = method
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }

            return try {
                print(response.bodyAsText())
                response.body<T>()
            } catch (e: Exception) {
                onError("Failed to parse response body", HttpStatusCode.InternalServerError)
                e.printStackTrace()
                null
            }
        } catch (e: ResponseException) {
            onError(e.message ?: "", e.response.status)
            e.printStackTrace()
            return null
        }
    }


    suspend fun requestWithBody(
        url: String,
        body: Any,
        method: HttpMethod,
        onError: (error: String, status: HttpStatusCode) -> Unit = { _, _ ->
            run {
            }
        },
    ): Boolean =
        try {
            httpClient.request(url) {
                this.method = method
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            true
        } catch (e: ServerResponseException) {
            onError(e.message, e.response.status)
            e.printStackTrace()
            false
        }

}