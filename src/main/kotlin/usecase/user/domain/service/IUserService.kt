package ord.pumped.usecase.user.domain.service

import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.rest.request.UserUpdateProfileRequest
import java.util.*

interface IUserService {
    fun registerUser(receiveAPIRequest: User): User
    fun loginUser(email: String, password: String): User
    fun getUser(userID: UUID): User
    fun getUserOrNull(userID: UUID): User?
    fun updateUserProfile(userID: UUID, receive: UserUpdateProfileRequest): User
    fun changePassword(userID: UUID, oldPassword: String, newPassword: String)
    fun deleteUser(userID: UUID, password: String)
}
