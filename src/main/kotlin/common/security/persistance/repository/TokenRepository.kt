package ord.pumped.common.security.persistance.repository

import ord.pumped.common.IRepository
import ord.pumped.common.security.domain.model.Token
import ord.pumped.common.security.persistance.dto.TokenDTO
import ord.pumped.common.security.persistance.dto.Tokens
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class TokenRepository: IRepository<Token, TokenDTO> {
    override fun save(token: Token): TokenDTO {
        return transaction {
            TokenDTO.new {
                this.isBlacklisted = token.isBlacklisted
            }
        }
    }

    override fun update(token: Token): TokenDTO {
        return transaction {
            TokenDTO.findByIdAndUpdate(token.id) {
                it.isBlacklisted = token.isBlacklisted
            }!!
        }
    }

    override fun delete(id: UUID) {
        return transaction {
            Tokens.deleteWhere { Tokens.id eq id }
        }
    }

    override fun findByID(id: UUID): TokenDTO? {
        return transaction {
            TokenDTO.findById(id)
        }
    }
}