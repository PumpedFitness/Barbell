package ord.pumped.common.security.persistance.dto

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID

object Tokens: UUIDTable("barbell_jwt_tokens") {
    val isBlacklisted = bool("is_blacklisted").default(false)
}

class TokenDTO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TokenDTO>(Tokens)

    var isBlacklisted: Boolean by Tokens.isBlacklisted
}