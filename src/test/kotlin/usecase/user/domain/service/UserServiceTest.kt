package usecase.user.domain.service

import at.favre.lib.crypto.bcrypt.BCrypt
import io.mockk.*
import ord.pumped.usecase.user.domain.mapper.UserModelMapper
import ord.pumped.usecase.user.domain.model.User
import ord.pumped.usecase.user.domain.service.IUserService
import ord.pumped.usecase.user.domain.service.UserServiceAdapter
import ord.pumped.usecase.user.exceptions.EmailAlreadyUsedException
import ord.pumped.usecase.user.exceptions.InvalidPasswordException
import ord.pumped.usecase.user.exceptions.UserNotFoundException
import ord.pumped.usecase.user.persistence.dto.UserDTO
import ord.pumped.usecase.user.persistence.repository.IUserRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import usecase.user.testfixtures.UserMother.Companion.createValidUser
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
            val user = createValidUser()
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
            val user = createValidUser()
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
            val user = createValidUser().copy(password = plainPassword)

            val capturedUserSlot = slot<User>()
            every { userRepository.findByEmail(user.email) } returns null
            every { userRepository.save(capture(capturedUserSlot)) } answers { mockk<UserDTO>() }
            every { userModelMapper.toDomain(any()) } returns user

            // Act
            userService.registerUser(user)

            // Assert
            val savedUser = capturedUserSlot.captured
            assertNotEquals("Password should be hashed", savedUser.password, plainPassword)
            assertTrue(savedUser.password.startsWith("\$2a\$"))
        }

        @Test
        fun `should map saved UserDTO to domain User`() {
            // Arrange
            val user = createValidUser()
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
            val user = createValidUser()
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
            val user = createValidUser()
            every { userRepository.findByEmail(any()) } returns null

            // Act / Assert
            assertThrows<UserNotFoundException> {
                userService.loginUser(user.email, user.password)
            }
        }

        @Test
        fun `should throw InvalidPasswordException when password is incorrect`() {
            // Arrange
            val user = createValidUser()
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


}
