package app.pumped.api

import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.Serializable

/**
 * @author Devin Fritz
 */
interface APIRequest {

    fun validate(): ValidationResult

}