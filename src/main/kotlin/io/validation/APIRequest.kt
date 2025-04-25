package io.validation

import io.ktor.server.plugins.requestvalidation.*

/**
 * @author Devin Fritz
 */
fun interface APIRequest {

    /**
     * Validates this request
     * @return Validation Result
     */
    fun validate(): ValidationResult

}