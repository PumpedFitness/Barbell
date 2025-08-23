package ord.pumped.usecase.user.domain.service

import at.favre.lib.crypto.bcrypt.BCrypt
import kotlinx.datetime.Clock
import ord.pumped.usecase.user.domain.mapper.UserModelMapper
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.exceptions.EmailAlreadyUsedException
import ord.pumped.usecase.user.exceptions.InvalidPasswordException
import ord.pumped.usecase.user.exceptions.UserNotFoundException
import ord.pumped.usecase.user.persistence.repository.IUserRepository
import ord.pumped.usecase.user.rest.request.UserUpdateProfileRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class UserServiceAdapter : IUserService, KoinComponent {

    val userRepository: IUserRepository by inject()
    val userModelMapper: UserModelMapper by inject()

    override fun registerUser(receiveAPIRequest: User): User {
        receiveAPIRequest.password = hashPassword(receiveAPIRequest.password)

        if (userRepository.findByEmail(receiveAPIRequest.email) != null) {
            throw EmailAlreadyUsedException()
        }

        val savedUser = userRepository.save(receiveAPIRequest)
        return userModelMapper.toDomain(savedUser)
    }

    override fun loginUser(email: String, password: String): User {
        val existingUser = userRepository.findByEmail(email) ?: throw UserNotFoundException()
        val mappedExistingUser = userModelMapper.toDomain(existingUser)
        validatePassword(mappedExistingUser.password, password)
        return mappedExistingUser
    }

    override fun getUser(userID: UUID): User {
        return getUserOrNull(userID) ?: throw UserNotFoundException()
    }

    override fun getUserOrNull(userID: UUID): User? {
        val user = userRepository.findByID(userID) ?: return null
        return userModelMapper.toDomain(user)
    }

    override fun updateUserProfile(
        userID: UUID,
        receive: UserUpdateProfileRequest
    ): User {
        var existingUser = getUser(userID)
        existingUser = existingUser.copy(
            updatedAt = Clock.System.now(),
            username = receive.username ?: existingUser.username,
            description = receive.description ?: existingUser.description,
            profilePicture = receive.profilePicture ?: existingUser.profilePicture
        )
        return userModelMapper.toDomain(userRepository.update(existingUser))
    }

    override fun changePassword(userID: UUID, oldPassword: String, newPassword: String) {
        var existingUser = getUser(userID)
        validatePassword(existingUser.password, oldPassword)
        existingUser = existingUser.copy(
            password = hashPassword(newPassword)
        )
        userRepository.update(existingUser)
    }

    override fun deleteUser(userID: UUID, password: String) {
        val existingUser = getUser(userID)
        validatePassword(existingUser.password, password)
        userRepository.delete(existingUser.id!!)
    }

    private fun validatePassword(userPassword: String, requestPassword: String) {
        val passwordVerificationResult = BCrypt.verifyer().verify(requestPassword.toCharArray(), userPassword)
        if (!passwordVerificationResult.verified) {
            throw InvalidPasswordException()
        }
    }

    private fun hashPassword(password: String): String {
        return BCrypt.withDefaults()
            .hashToString(12, password.toCharArray())
    }
}