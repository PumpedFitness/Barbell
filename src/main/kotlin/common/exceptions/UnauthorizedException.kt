package ord.pumped.common.exceptions

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondText
import ord.pumped.common.APIException

class UnauthorizedException : APIException("Unauthorized") {

    override suspend fun handle(call: ApplicationCall) {
        call.respondText("You are not authorized to perform this operation", status = HttpStatusCode.Unauthorized)
    }

}