package ord.pumped.common.service

import io.ktor.server.application.*
import java.util.UUID

fun interface ISecurityService {

    /**
     * Creates a jwt token with the default timeout with a claim for the given user
     */
    fun createJWTToken(application: Application, userID: UUID): String
}