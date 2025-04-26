package ord.pumped.usecase.user.domain.service

import at.favre.lib.crypto.bcrypt.BCrypt
import ord.pumped.usecase.user.domain.mapper.UserModelMapper
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.exceptions.InvalidPasswordException
import ord.pumped.usecase.user.exceptions.UserNotFoundException
import ord.pumped.usecase.user.persistence.repository.UserRepository
import ord.pumped.usecase.user.rest.controller.UserController.userService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

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
        val passordVerificationResult = BCrypt.verifyer().verify(password.toCharArray(), existingUser.password)
        if (!passordVerificationResult.verified){
            throw InvalidPasswordException()
        }
        return userService.loginUser(email, password)
    }
}