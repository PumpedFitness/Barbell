package ord.pumped.common.security.service

import io.ktor.server.application.*
import ord.pumped.common.security.domain.model.Token
import java.util.UUID

interface ISecurityService {

    /**
     * Creates a jwt token with the default timeout with a claim for the given user
     */
    fun createJWTToken(application: Application, userID: UUID): Token

    /**
     * Blacklists the token with the given id
     */
    fun blacklistToken(tokenID: UUID)
}