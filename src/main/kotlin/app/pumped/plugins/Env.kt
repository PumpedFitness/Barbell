package app.pumped.plugins

import app.pumped.io.env.Env
import io.ktor.server.application.*

private lateinit var lateEnv: Env

fun Application.configureEnv() {
    lateEnv = Env(this.log)
}

val Application.env
    get() = lateEnv