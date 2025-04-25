package ord.pumped.usecase.user.exceptions

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondText
import ord.pumped.common.APIException

class InvalidPasswordException: APIException("Invalid Password") {

    override suspend fun handle(call: ApplicationCall) {
        call.respondText("Invalid Password", status = HttpStatusCode.BadRequest)
    }

}