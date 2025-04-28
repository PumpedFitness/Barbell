package ord.pumped.usecase.user.domain.service

import ord.pumped.usecase.user.domain.model.User
import java.util.*

interface IUserService {
    fun registerUser(receiveAPIRequest: User): User
    fun loginUser(email: String, password: String): User
    fun getUser(userID: UUID): User
}
