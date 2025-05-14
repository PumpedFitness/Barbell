package usecase.user.domain.service

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import ord.pumped.usecase.user.domain.service.IUserService
import ord.pumped.usecase.user.exceptions.EmailAlreadyUsedException
import ord.pumped.usecase.user.persistence.dto.UserDTO
import ord.pumped.usecase.user.persistence.repository.IUserRepository
import org.junit.jupiter.api.*
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import usecase.user.serviceModule
import usecase.user.testfixtures.UserMother

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest : KoinTest {

    val userService: IUserService by inject()
    private val userRepository = mockk<IUserRepository>()

    @BeforeEach
    fun setup() {
        clearMocks(userRepository)
        startKoin {
            modules(serviceModule)
        }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `should throw EmailAlreadyUsedException if email is found`() {
        //given
        val user = UserMother.createValidUser()
        val mockUserDto = mockk<UserDTO>()

        //when
        every { userRepository.findByEmail(any()) } returns mockUserDto

        //then
        assertThrows<EmailAlreadyUsedException> {
            userService.registerUser(user)
        }
    }
}
