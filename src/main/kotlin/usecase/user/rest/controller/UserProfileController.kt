package ord.pumped.usecase.user.rest.controller

import ord.pumped.usecase.user.rest.request.UserUpdateProfileRequest
import org.koin.core.component.KoinComponent
import java.util.*

object UserProfileController : KoinComponent {

    fun postUserProfile(userID: UUID, receive: UserUpdateProfileRequest) {
        TODO("Not yet implemented")
    }

}