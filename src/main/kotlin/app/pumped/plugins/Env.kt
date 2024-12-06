package app.pumped.plugins

import app.pumped.io.env.Env
import io.ktor.server.application.*

lateinit var globalEnv: Env

fun Application.configureEnv() {
    globalEnv = Env(this.log)
}

val Application.env
    get() = globalEnv