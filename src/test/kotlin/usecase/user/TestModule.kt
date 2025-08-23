package usecase.user

import ord.pumped.usecase.user.domain.service.IUserService
import ord.pumped.usecase.user.domain.service.UserServiceAdapter
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val serviceModule = module {
    singleOf(::UserServiceAdapter) { bind<IUserService>() }
}