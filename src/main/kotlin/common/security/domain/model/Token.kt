package ord.pumped.common.security.domain.model

import java.util.UUID

data class Token(val id: UUID, val isBlacklisted: Boolean, val jwt: String = "")
