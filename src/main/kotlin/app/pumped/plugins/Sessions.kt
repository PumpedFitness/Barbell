package app.pumped.plugins

import io.github.hmiyado.ktor.csrfprotection.CsrfTokenBoundClient
import io.github.hmiyado.ktor.csrfprotection.CsrfTokenSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

@Serializable
data class ClientSession(
    val token: String
): CsrfTokenBoundClient {
    override val representation: String = token
}

fun Application.configureSessions() {
    install(Sessions) {
        val storage = SessionStorageMemory()
        cookie<ClientSession>("client_session", storage = storage)
        //header<CsrfTokenSession>("X-CSRF-TOKEN", storage = storage)
    }
}