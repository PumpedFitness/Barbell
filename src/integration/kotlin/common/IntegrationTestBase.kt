package common

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
                withExposedPorts(dotenv["BB_DB_PORT"].toInt())
                withPassword(dotenv["BB_DB_PASSWORD"])
                portBindings = listOf(
                    "${dotenv["BB_DB_PORT"]}:${dotenv["BB_DB_PORT"]}"
                )
                start()
            }
            println("started mariadb container with name: ${db.containerName}, id: ${db.containerId}, and url: ${db.jdbcUrl}, user: ${db.username}, password: ${db.password}, database: ${db.databaseName}, port: ${db.firstMappedPort}, and host: ${db.host}")

            redisContainer = object : GenericContainer<Nothing>("redis:8.0.0") {}.apply {
                withExposedPorts(dotenv["BB_REDIS_PORT"].toInt())
                portBindings = listOf(
                    "${dotenv["BB_REDIS_PORT"]}:${dotenv["BB_REDIS_PORT"]}"
                )
                start()
            }
            println(
                "started redis container with name: ${redisContainer.containerName}, id: ${redisContainer.containerId}, and port: ${
                    redisContainer.getMappedPort(
                        dotenv["BB_REDIS_PORT"].toInt()
                    )
                }"
            )

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