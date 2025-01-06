package app.pumped.domain

import io.ktor.server.routing.*

/**
 * @author Devin Fritz
 */
interface ModelRouter<M> {

    fun Route.route()

}