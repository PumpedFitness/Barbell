package ord.pumped.usecase.user.domain.service

import ord.pumped.usecase.user.domain.mapper.UserModelMapper
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.persistence.repository.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserServiceAdapter : IUserService, KoinComponent {

    val userRepository: UserRepository by inject()
    val userModelMapper: UserModelMapper by inject()

    override fun registerUser(receiveAPIRequest: User): User {
        val savedUser = userRepository.save(receiveAPIRequest)
        return userModelMapper.toDomain(savedUser)
    }
}