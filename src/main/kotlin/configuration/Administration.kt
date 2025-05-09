package ord.pumped.configuration

import io.github.flaxoos.ktor.server.plugins.ratelimiter.RateLimiting
import io.github.flaxoos.ktor.server.plugins.ratelimiter.implementations.TokenBucket
import io.github.flaxoos.ktor.server.plugins.taskscheduling.TaskScheduling
import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.lock.redis.redis
import io.ktor.server.application.*
import io.ktor.server.routing.*
import ord.pumped.io.env.EnvVariables
import kotlin.time.Duration.Companion.seconds

fun Application.configureAdministration() {
    routing {
        route("/") {
            install(RateLimiting) {
                rateLimiter {
                    type = TokenBucket::class
                    capacity = 100
                    rate = 10.seconds
                }
            }
        }
    }
    install(TaskScheduling) {
        redis {
            connectionPoolInitialSize = 1
            host = secrets[EnvVariables.BB_REDIS_HOST]
            port = secrets[EnvVariables.BB_REDIS_PORT].toInt()
            connectionAcquisitionTimeoutMs = 1_000
            lockExpirationMs = 60_000
        }

        task {
            name = "Health Check"
            concurrency = 1
            task = {
                if (!pingDatabase()) {
                    log.error("Could not ping database. Killing Application")
                    engine.stop()
                }
            }
            kronSchedule = {
                seconds {
                    every(30)
                }
            }
        }
}}
