package ord.pumped.usecase.user.rest.request

data class UserUpdateProfileRequest(
    val username: String?,
    val description: String?,
    val profilePicture: String?
)
