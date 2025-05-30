package ord.pumped.io.websocket.auth.exception

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondText
import ord.pumped.common.APIException

class UnknownSocketException: APIException("Unknown Socket") {
    override suspend fun handle(call: ApplicationCall) {
        call.respondText("Unknown websocket", status = HttpStatusCode.BadRequest)
    }
}