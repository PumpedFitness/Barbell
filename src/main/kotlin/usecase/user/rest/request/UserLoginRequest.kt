package ord.pumped.usecase.user.rest.request

data class UserLoginRequest(
    val email: String,
    val password: String,
)
