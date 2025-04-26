package ord.pumped.usecase.user.rest.response

data class UserLoginResponse(
    val email: String,
    val username: String,
    val Token: String? = null,
)
