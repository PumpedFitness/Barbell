package ord.pumped.common.security.service

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

object SecurityController : KoinComponent {
    private val securityService: ISecurityService by inject()

    fun blacklistToken(tokenID: UUID) {
        securityService.blacklistToken(tokenID)
    }
}