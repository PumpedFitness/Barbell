package app.pumped.data.context

import io.ktor.server.application.*

/**
 * @author Devin Fritz
 */
interface RouteContext: Context {
    val call: ApplicationCall
}

class KoinRouteContext(override val call: ApplicationCall) : RouteContext
