package ord.pumped.usecase.user.domain.service

import at.favre.lib.crypto.bcrypt.BCrypt
import ord.pumped.usecase.user.domain.mapper.UserModelMapper
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.exceptions.InvalidPasswordException
import ord.pumped.usecase.user.exceptions.UserNotFoundException
import ord.pumped.usecase.user.persistence.repository.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class UserServiceAdapter : IUserService, KoinComponent {

    val userRepository: UserRepository by inject()
    val userModelMapper: UserModelMapper by inject()

    override fun registerUser(receiveAPIRequest: User): User {
        receiveAPIRequest.password = BCrypt.withDefaults()
            .hashToString(12, receiveAPIRequest.password.toCharArray())
        val savedUser = userRepository.save(receiveAPIRequest)
        return userModelMapper.toDomain(savedUser)
    }

    override fun loginUser(email: String, password: String): User {
        val existingUser = userRepository.findByEmail(email) ?: throw UserNotFoundException()
        val passwordVerificationResult = BCrypt.verifyer().verify(password.toCharArray(), existingUser.password)
        if (!passwordVerificationResult.verified) {
            throw InvalidPasswordException()
        }
        return userModelMapper.toDomain(existingUser)
    }

    override fun getUser(userID: UUID): User {
        val user = userRepository.findByID(userID) ?: throw UserNotFoundException()
        return userModelMapper.toDomain(user)
    }
}