package ord.pumped.usecase.user.persistence.repository

import ord.pumped.common.IRepository
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.persistence.dto.UserDTO

interface IUserRepository : IRepository<User, UserDTO> {
    fun findByEmailLogic(email: String): UserDTO?
    fun findByEmail(email: String): UserDTO? = withLogging("findByEmail", "email" to email) {
        findByEmailLogic(email)
    }
}