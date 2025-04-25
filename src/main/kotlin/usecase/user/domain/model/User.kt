package ord.pumped.usecase.user.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.*

data class User(
    val id: UUID?,
    var username: String,
    var password: String,
    var email: String,
    val createdAt: Instant = Clock.System.now(),
    var updatedAt: Instant = Clock.System.now()
)
