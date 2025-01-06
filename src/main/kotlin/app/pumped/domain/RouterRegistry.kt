package app.pumped.domain

import app.pumped.domain.user.UserRouter
import app.pumped.util.Registry

object RouterRegistry: Registry<ModelRouter<*>>() {
    fun registerRouters() {
        register(UserRouter())
    }


}