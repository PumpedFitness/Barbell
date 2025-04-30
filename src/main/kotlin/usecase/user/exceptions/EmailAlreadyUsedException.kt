package ord.pumped.usecase.user.exceptions

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ord.pumped.common.APIException

class EmailAlreadyUsedException : APIException("Email is already used") {
    override suspend fun handle(call: ApplicationCall) {
        call.respondText("Email is already used", status = HttpStatusCode.Conflict)
    }
}