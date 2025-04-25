package ord.pumped.usecase.user

import ord.pumped.common.IRepository
import ord.pumped.usecase.user.domain.mapper.UserModelMapper
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.domain.service.IUserService
import ord.pumped.usecase.user.domain.service.UserServiceAdapter
import ord.pumped.usecase.user.persistence.dto.UserDTO
import ord.pumped.usecase.user.persistence.repository.UserRepository
import ord.pumped.usecase.user.rest.mapper.UserRequestMapper
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userModule = module {
    singleOf(::UserRequestMapper)
    singleOf(::UserModelMapper)
    singleOf(::UserServiceAdapter) { bind<IUserService>() }
    singleOf(::UserRepository) { bind<IRepository<User, UserDTO>>() }
}