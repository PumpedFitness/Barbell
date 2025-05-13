package ord.pumped.usecase.user.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OnlineUser(
    val id: String,
    var username: String,
    val description: String? = "",
    val profilePicture: String? = "",
)
