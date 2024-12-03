package app.pumped.plugins

import io.github.hmiyado.ktor.csrfprotection.Csrf
import io.github.hmiyado.ktor.csrfprotection.header
import io.github.hmiyado.ktor.csrfprotection.session
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureCSRF() {
    install(Csrf) {
        requestFilter { httpMethod, path ->
            path == "/api" && httpMethod in listOf(HttpMethod.Post)
        }
        // use csrf token with Ktor Sessions plugin
        session<ClientSession> {
            onFail { this.respond(HttpStatusCode.Forbidden) }
        }

        header {
            validator { headers ->
                headers.entries().any { (k, _) -> k.uppercase() == "X-CSRF-TOKEN" }
            }
            onFail { respond(HttpStatusCode.Forbidden) }
        }
    }
}