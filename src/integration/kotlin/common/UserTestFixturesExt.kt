import kotlinx.datetime.Instant
import ord.pumped.usecase.user.rest.request.UserDeleteUserRequest
import ord.pumped.usecase.user.rest.request.UserLoginRequest
import ord.pumped.usecase.user.rest.request.UserRegisterRequest
import ord.pumped.usecase.user.rest.request.UserUpdatePasswordRequest
import ord.pumped.usecase.user.rest.request.UserUpdateProfileRequest
import ord.pumped.usecase.user.rest.response.UserLoginResponse
import ord.pumped.usecase.user.rest.response.UserMeResponse
import ord.pumped.usecase.user.rest.response.UserRegisterResponse

const val TEST_MAIL: String = "test@pumped.de"
const val TEST_USER: String = "test@pumped.de"

fun UserLoginRequest.Companion.testRequest(): UserLoginRequest {
    return UserLoginRequest(
        email = TEST_MAIL,
        password = "12345678"
    )
}


fun UserRegisterRequest.Companion.testRequest(): UserRegisterRequest {
    return UserRegisterRequest(
        email = TEST_MAIL,
        password = "12345678",
        username = TEST_USER
    )
}


fun UserLoginResponse.Companion.testResponse(): UserLoginResponse {
    return UserLoginResponse(
        email = TEST_MAIL,
        username = TEST_USER
    )
}


fun UserDeleteUserRequest.Companion.testRequest(): UserDeleteUserRequest {
    return UserDeleteUserRequest(
        password = "newPassword123"
    )
}

fun UserUpdateProfileRequest.Companion.testRequest(): UserUpdateProfileRequest {
    return UserUpdateProfileRequest(
        username = "Change",
        description = "Description",
        profilePicture = "ProfilePicture"
    )
}


fun UserUpdatePasswordRequest.Companion.testRequest(): UserUpdatePasswordRequest {
    return UserUpdatePasswordRequest(
        oldPassword = "12345678",
        newPassword = "newPassword123"
    )
}

fun UserRegisterResponse.Companion.testResponse(): UserRegisterResponse {
    return UserRegisterResponse(
        username = TEST_USER,
        email = TEST_MAIL,
        createdAt = Instant.fromEpochMilliseconds(1000),
        updatedAt = Instant.fromEpochMilliseconds(1000)
    )
}

fun UserMeResponse.Companion.testResponse(): UserMeResponse {
    return UserMeResponse(
        id = "some id",
        username = TEST_USER,
        email = TEST_MAIL,
        createdAt = Instant.fromEpochMilliseconds(123),
        updatedAt = Instant.fromEpochMilliseconds(123),
        description = "this is a description",
        profilePicture = "this is a picture"
    )
}