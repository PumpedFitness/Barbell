package ord.pumped.usecase.user.exceptions

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ord.pumped.common.APIException

class UserIDWrongFormatException : APIException("UserID has to be a valid UUID") {

    override suspend fun handle(call: ApplicationCall) {
        call.respondText("UserID has to be a valid UUID", status = HttpStatusCode.BadRequest)

    }
}