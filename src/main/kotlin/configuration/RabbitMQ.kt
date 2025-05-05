package ord.pumped.configuration

import io.github.damir.denis.tudor.ktor.server.rabbitmq.RabbitMQ
import io.ktor.server.application.Application
import io.ktor.server.application.install
import ord.pumped.io.env.EnvVariables
import ord.pumped.io.env.env

fun Application.configureRabbitMQ() {
    install(RabbitMQ) {
        uri = "amqp://${env[EnvVariables.BB_RABBITMQ_USER]}:${env[EnvVariables.BB_RABBITMQ_PASSWORD]}@${env[EnvVariables.BB_RABBITMQ_HOST]}:${env[EnvVariables.BB_RABBITMQ_PORT]}"
        defaultConnectionName = "default-connection"
        dispatcherThreadPollSize = 4
        tlsEnabled = false
    }
}