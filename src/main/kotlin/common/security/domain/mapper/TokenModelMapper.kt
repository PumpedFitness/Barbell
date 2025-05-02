package ord.pumped.common.security.domain.mapper

import ord.pumped.common.mapping.IModelMapper
import ord.pumped.common.security.domain.model.Token
import ord.pumped.common.security.persistance.dto.TokenDTO
import org.jetbrains.exposed.sql.transactions.transaction

class TokenModelMapper: IModelMapper<Token, TokenDTO> {
    override fun toDomain(dto: TokenDTO): Token {
        return transaction {
            Token(
                dto.id.value,
                dto.isBlacklisted
            )
        }
    }
}