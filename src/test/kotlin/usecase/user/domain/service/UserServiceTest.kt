package usecase.user.domain.service

import at.favre.lib.crypto.bcrypt.BCrypt
import io.mockk.*
import ord.pumped.usecase.user.domain.mapper.UserModelMapper
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.domain.model.validTestData
import ord.pumped.usecase.user.domain.service.IUserService
import ord.pumped.usecase.user.domain.service.UserServiceAdapter
import ord.pumped.usecase.user.exceptions.EmailAlreadyUsedException
import ord.pumped.usecase.user.exceptions.InvalidPasswordException
import ord.pumped.usecase.user.exceptions.UserNotFoundException
import ord.pumped.usecase.user.persistence.dto.UserDTO
import ord.pumped.usecase.user.persistence.repository.IUserRepository
import ord.pumped.usecase.user.rest.request.UserUpdateProfileRequest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import java.util.*
import kotlin.test.junit.JUnitAsserter.assertNotEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest : KoinTest {

    private lateinit var userService: IUserService
    private val userRepository = mockk<IUserRepository>(relaxed = true)
    private val userModelMapper = mockk<UserModelMapper>(relaxed = true)

    @BeforeEach
    fun setup() {
        clearMocks(userRepository, userModelMapper)
        startKoin {
            modules(module {
                single<IUserRepository> { userRepository }
                single<UserModelMapper> { userModelMapper }
                single<IUserService> { UserServiceAdapter() }
            })
        }
        userService = get()
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Nested
    inner class RegistrationTests {
        @Test
        fun `should throw EmailAlreadyUsedException if email is found`() {
            // Arrange
            val user = User.validTestData()
            val mockUserDto = mockk<UserDTO>()

            every { userRepository.findByEmail(any()) } returns mockUserDto

            // Act/Assert
            assertThrows<EmailAlreadyUsedException> {
                userService.registerUser(user)
            }
        }

        @Test
        fun `should verify happy path`() {
            // Arrange
            val user = User.validTestData()

            val userDto = mockk<UserDTO>()

            every { userRepository.findByEmail(any()) } returns null
            every { userRepository.save(user) } returns userDto
            every { userModelMapper.toDomain(any()) } returns user

            //Act
            userService.registerUser(user)

            // Assert
            verify(exactly = 1) { userRepository.save(user) }
            verify(exactly = 1) { userModelMapper.toDomain(userDto) }
            verify(exactly = 1) { userRepository.save(user) }
        }

        @Test
        fun `should hash the password before saving the user`() {
            // Arrange
            val plainPassword = "mySecret123"
            val user = User.validTestData().copy(password = plainPassword)

            val capturedUserSlot = slot<User>()
            every { userRepository.findByEmail(user.email) } returns null
            every { userRepository.save(capture(capturedUserSlot)) } answers { mockk<UserDTO>() }
            every { userModelMapper.toDomain(any()) } returns user

            // Act
            userService.registerUser(user)

            // Assert
            val savedUser = capturedUserSlot.captured
            assertNotEquals("Password should be hashed", savedUser.password, plainPassword)
            assertTrue(savedUser.password.startsWith("$2a$"))
        }

        @Test
        fun `should map saved UserDTO to domain User`() {
            // Arrange
            val user = User.validTestData()

            val mockDto = mockk<UserDTO>()
            val mappedUser = mockk<User>()

            every { userRepository.findByEmail(user.email) } returns null
            every { userRepository.save(any()) } returns mockDto
            every { userModelMapper.toDomain(mockDto) } returns mappedUser

            // Act
            val result = userService.registerUser(user)

            // Assert
            assertEquals(mappedUser, result)
            verify(exactly = 1) { userModelMapper.toDomain(mockDto) }
        }
    }

    @Nested
    inner class LoginTests {

        @Test
        fun `should return user when credentials are correct`() {
            // Arrange
            val user = User.validTestData()

            val rawPassword = "securePassword123"
            val hashedPassword = BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray())
            val userDTO = mockk<UserDTO>(relaxed = true)
            every { userRepository.findByEmail(user.email) } returns userDTO
            val mappedUser = user.copy(password = hashedPassword)
            every { userModelMapper.toDomain(userDTO) } returns mappedUser

            // Act
            val result = userService.loginUser(user.email, rawPassword)

            // Assert
            assertEquals(mappedUser, result)
        }

        @Test
        fun `should throw UserNotFoundException if email is not found`() {
            // Arrange
            val user = User.validTestData()

            every { userRepository.findByEmail(any()) } returns null

            // Act / Assert
            assertThrows<UserNotFoundException> {
                userService.loginUser(user.email, user.password)
            }
        }

        @Test
        fun `should throw InvalidPasswordException when password is incorrect`() {
            // Arrange
            val user = User.validTestData()

            val wrongPassword = "wrongPassword"
            val hashedPassword = BCrypt.withDefaults().hashToString(12, user.password.toCharArray())
            val userDTO = mockk<UserDTO>(relaxed = true)
            every { userRepository.findByEmail(user.email) } returns userDTO
            val mappedUser = user.copy(password = hashedPassword)
            every { userModelMapper.toDomain(userDTO) } returns mappedUser

            // Act / Assert
            assertThrows<InvalidPasswordException> {
                userService.loginUser(user.email, wrongPassword)
            }
        }

    }

    @Nested
    inner class UpdateTests {

        @Test
        fun `should update user profile successfully`() {
            // Arrange
            val existingUser = User.validTestData()


            val updateRequest = UserUpdateProfileRequest(
                username = existingUser.username,
                description = "New description",
                profilePicture = "new.png"
            )

            val updatedUser = existingUser.copy(
                description = updateRequest.description,
                profilePicture = updateRequest.profilePicture
            )

            every { userRepository.findByID(existingUser.id!!) } returns mockk(relaxed = true) // Needed by getUser()
            every { userModelMapper.toDomain(any()) } returns updatedUser
            every { userRepository.update(any()) } returns mockk() // can mock the dto here

            // Act
            val result = userService.updateUserProfile(existingUser.id!!, updateRequest)

            // Assert
            assertEquals(updateRequest.description, result.description)
            assertEquals(updateRequest.profilePicture, result.profilePicture)
            verify { userRepository.update(any()) }
        }

        @Test
        fun `should throw UserNotFoundException when user is not found`() {
            // Arrange
            val userId = UUID.randomUUID()
            val updateRequest = UserUpdateProfileRequest(
                username = "john barbell",
                description = "New desc",
                profilePicture = "new.png"
            )
            every { userRepository.findByID(userId) } returns null

            // Assert / Act
            assertThrows<UserNotFoundException> {
                userService.updateUserProfile(userId, updateRequest)
            }
        }
    }

    @Nested
    inner class DeleteTests {

        @Test
        fun `should delete user when password matches`() {
            // Arrange
            val plainPassword = "validPass123"
            val hashedPassword = BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray())
            val mockUser = User.validTestData().copy(password = hashedPassword)
            every { userRepository.findByID(mockUser.id!!) } returns mockk()
            every { userModelMapper.toDomain(any()) } returns mockUser
            every { userRepository.delete(mockUser.id!!) } just Runs

            //Act
            userService.deleteUser(mockUser.id!!, plainPassword)

            // Assert
            verify(exactly = 1) { userRepository.delete(mockUser.id) }
        }

        @Test
        fun `should throw UserNotFoundException when user does not exist`() {
            // Arrange
            val fakeUserId = UUID.randomUUID()
            every { userRepository.findByID(fakeUserId) } returns null

            // Act
            assertThrows<UserNotFoundException> {
                userService.deleteUser(fakeUserId, "anyPassword")
            }

            // Assert
            verify(exactly = 0) { userRepository.delete(any()) }
        }

        @Test
        fun `should throw InvalidPasswordException when password is incorrect`() {
            // Arrange
            val plainPassword = "correctPassword"
            val wrongPassword = "wrongPassword"
            val hashedPassword = BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray())
            val mockUser = User.validTestData().copy(password = hashedPassword)
            every { userRepository.findByID(mockUser.id!!) } returns mockk()
            every { userModelMapper.toDomain(any()) } returns mockUser

            // Act
            assertThrows<InvalidPasswordException> {
                userService.deleteUser(mockUser.id!!, wrongPassword)
            }

            // Assert
            verify(exactly = 0) { userRepository.delete(any()) }
        }
    }

    @Nested
    inner class ChangePasswordTests {

        @Test
        fun `should change password when old password is correct`() {
            // Arrange
            val oldPassword = "oldPass123"
            val newPassword = "newPass456"
            val hashedOldPassword = BCrypt.withDefaults().hashToString(12, oldPassword.toCharArray())
            val user = User.validTestData().copy(password = hashedOldPassword)
            val updatedUserSlot = slot<User>()
            every { userRepository.findByID(user.id!!) } returns mockk()
            every { userModelMapper.toDomain(any()) } returns user
            every { userRepository.update(capture(updatedUserSlot)) } returns mockk()

            // Act
            userService.changePassword(user.id!!, oldPassword, newPassword)

            // Assert
            val updatedUser = updatedUserSlot.captured
            verify(exactly = 1) { userRepository.update(capture(updatedUserSlot)) }
            assertTrue(updatedUser.password != hashedOldPassword, "Password should be changed and rehashed")
            assertTrue(updatedUser.password.startsWith("$2a$"), "Password should be BCrypt hashed")
        }

        @Test
        fun `should throw UserNotFoundException when user does not exist`() {
            // Arrange
            val userId = UUID.randomUUID()
            every { userRepository.findByID(userId) } returns null

            // Act
            assertThrows<UserNotFoundException> {
                userService.changePassword(userId, "any", "any")
            }

            // Assert
            verify(exactly = 0) { userRepository.update(any()) }
        }

        @Test
        fun `should throw InvalidPasswordException when old password does not match`() {
            // Arrange
            val oldPassword = "correctOld"
            val wrongPassword = "wrongOld"
            val hashedOldPassword = BCrypt.withDefaults().hashToString(12, oldPassword.toCharArray())
            val user = User.validTestData().copy(password = hashedOldPassword)

            every { userRepository.findByID(user.id!!) } returns mockk()
            every { userModelMapper.toDomain(any()) } returns user

            // Act / Assert
            assertThrows<InvalidPasswordException> {
                userService.changePassword(user.id!!, wrongPassword, "newPass")
            }

            verify(exactly = 0) { userRepository.update(any()) }
        }
    }
}


