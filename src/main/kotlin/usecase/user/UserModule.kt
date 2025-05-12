package ord.pumped.usecase.user

import ord.pumped.usecase.user.domain.mapper.OnlineUserModelMapper
import ord.pumped.usecase.user.domain.mapper.UserModelMapper
import ord.pumped.usecase.user.domain.service.IUserService
import ord.pumped.usecase.user.domain.service.UserServiceAdapter
import ord.pumped.usecase.user.persistence.repository.IUserRepository
import ord.pumped.usecase.user.persistence.repository.UserRepository
import ord.pumped.usecase.user.rest.mapper.UserListRequestMapper
import ord.pumped.usecase.user.rest.mapper.UserLoginRequestMapper
import ord.pumped.usecase.user.rest.mapper.UserMeRequestMapper
import ord.pumped.usecase.user.rest.mapper.UserRegisterRequestMapper
import ord.pumped.usecase.user.rest.mapper.di.UserRequestMappers
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userModule = module {
    singleOf(::UserRegisterRequestMapper)
    singleOf(::UserModelMapper)
    singleOf(::UserLoginRequestMapper)
    singleOf(::UserMeRequestMapper)
    singleOf(::UserListRequestMapper)
    singleOf(::OnlineUserModelMapper)
    singleOf(::UserServiceAdapter) { bind<IUserService>() }
    singleOf(::UserRepository) { bind<IUserRepository>() }


    single {
        UserRequestMappers(
            register = get(),
            login = get(),
            me = get(),
            list = get()
        )
    }
}