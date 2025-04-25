package ord.pumped.usecase.user.domain.service

import ord.pumped.usecase.user.domain.model.User

fun interface IUserService {
    fun registerUser(receiveAPIRequest: User): User
}
