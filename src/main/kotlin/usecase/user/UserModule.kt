package ord.pumped.usecase.user

import ord.pumped.common.IRepository
import ord.pumped.usecase.user.domain.mapper.UserModelMapper
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.domain.service.IUserService
import ord.pumped.usecase.user.domain.service.UserServiceAdapter
import ord.pumped.usecase.user.persistence.dto.UserDTO
import ord.pumped.usecase.user.persistence.repository.UserRepository
import ord.pumped.usecase.user.rest.mapper.UserLoginRequestMapper
import ord.pumped.usecase.user.rest.mapper.UserRegisterRequestMapper
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userModule = module {
    singleOf(::UserRegisterRequestMapper)
    singleOf(::UserModelMapper)
    singleOf(::UserLoginRequestMapper)
    singleOf(::UserServiceAdapter) { bind<IUserService>() }
    singleOf(::UserRepository) { bind<IRepository<User, UserDTO>>() }
}