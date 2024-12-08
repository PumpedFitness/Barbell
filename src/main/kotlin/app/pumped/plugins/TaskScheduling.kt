package app.pumped.plugins

import app.pumped.io.env.EnvVariables
import io.github.flaxoos.ktor.server.plugins.taskscheduling.TaskScheduling
import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.lock.redis.redis
import io.ktor.server.application.*

fun Application.configureTaskScheduling() {
    install(TaskScheduling) {
        //https://github.com/Flaxoos/extra-ktor-plugins/tree/main/ktor-server-task-scheduling
        redis {
            this.host = env[EnvVariables.BB_REDIS_HOST]
            this.port = env[EnvVariables.BB_REDIS_PORT].toInt()
            this.password = env[EnvVariables.BB_REDIS_PASSWORD]
        }
    }
}