package ord.pumped.usecase.user.rest.mapper.di

import ord.pumped.usecase.user.rest.mapper.UserListRequestMapper
import ord.pumped.usecase.user.rest.mapper.UserLoginRequestMapper
import ord.pumped.usecase.user.rest.mapper.UserMeRequestMapper
import ord.pumped.usecase.user.rest.mapper.UserRegisterRequestMapper

class UserRequestMappers(
    val register: UserRegisterRequestMapper,
    val login: UserLoginRequestMapper,
    val me: UserMeRequestMapper,
    val list: UserListRequestMapper
)