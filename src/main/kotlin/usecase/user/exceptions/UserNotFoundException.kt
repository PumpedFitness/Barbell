package ord.pumped.usecase.user.exceptions

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondText
import ord.pumped.common.APIException

class UserNotFoundException: APIException("User not found") {

    override suspend fun handle(call: ApplicationCall) {
        call.respondText("User not found", status = HttpStatusCode.BadRequest)
    }
}