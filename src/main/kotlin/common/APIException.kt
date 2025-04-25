package ord.pumped.common

import io.ktor.server.application.ApplicationCall

abstract class APIException(reason: String): Exception(reason) {

    abstract suspend fun handle(call: ApplicationCall)

}