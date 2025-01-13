package app.pumped.domain.user

import app.pumped.domain.ModelRouter
import io.ktor.client.engine.*
import io.ktor.http.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

class UserRouter : ModelRouter<User> {
    override fun Route.route() {
        get("/") {
        }
    }
}
