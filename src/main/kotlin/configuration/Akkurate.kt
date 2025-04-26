package ord.pumped.configuration

import dev.nesk.akkurate.ktor.server.Akkurate
import io.ktor.server.application.*

/**
 * @author=henry
 */
fun Application.configureAkkurate() {
    install(Akkurate)


}