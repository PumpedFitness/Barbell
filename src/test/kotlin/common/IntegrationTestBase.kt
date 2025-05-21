package common

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.testing.*
import ord.pumped.common.security.service.securityModule
import ord.pumped.io.websocket.websocketModule
import ord.pumped.module
import ord.pumped.usecase.user.userModule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.test.KoinTest
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.utility.DockerImageName

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class IntegrationTestBase : KoinTest {
    companion object {
        private lateinit var db: MariaDBContainer<*>
        private lateinit var redisContainer: GenericContainer<Nothing>

        private val dotenv = dotenv {
            filename = ".env"
            ignoreIfMissing = false
        }

        @JvmStatic
        @BeforeAll
        fun globalSetup() {
            db = MariaDBContainer(DockerImageName.parse("mariadb:11.2.2")).apply {
                withDatabaseName(dotenv["BB_DB_DATABASE"])
                withUsername(dotenv["BB_DB_USER"])
                withPassword(dotenv["BB_DB_PASSWORD"])
                start()
            }

            redisContainer = object : GenericContainer<Nothing>("redis:8.0.0") {}.apply {
                withExposedPorts(dotenv["BB_REDIS_PORT"].toInt())
                withCreateContainerCmdModifier {
                    it.hostConfig?.withPortBindings(
                        PortBinding(
                            Ports.Binding
                                .bindPort(
                                    dotenv["BB_REDIS_PORT"]
                                        .toInt()
                                ),
                            ExposedPort(dotenv["BB_REDIS_PORT"].toInt())
                        )
                    )
                }
                start()
            }

            startKoin {
                modules(userModule, securityModule, websocketModule)
            }
        }

        @JvmStatic
        @AfterAll
        fun globalTearDown() {
            stopKoin()
            db.stop()
            redisContainer.stop()
        }
    }

    fun ApplicationTestBuilder.setupTestApplication() {
        application {
            module(true)
        }
    }
}