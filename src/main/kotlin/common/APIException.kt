package ord.pumped.common

import io.ktor.server.application.ApplicationCall
import ord.pumped.util.Loggable

abstract class APIException(reason: String): Exception(reason), Loggable {

    /**
     * Internal function of the handle function, so logging can be done in the handle function
     * @See Loggable
     */
    abstract suspend fun handleLogic(call: ApplicationCall)

    /**
     * The operation name to be logged.
     * @See Loggable
     */
    val logName: String = this::class.simpleName ?: "Unknown API Exception"

    /**
     * Returns the parameters to be logged.
     *
     * Please don't log sensitive information here, as it will be visible in the logs.
     *
     * The default parameter function for logging includes only the call parameters.
     * @See Loggable
     */
    val logParameterFunction: (call: ApplicationCall) ->  Pair<String, Any?> = { call -> "Call-parameters" to call.parameters }

    fun handle(call: ApplicationCall) = withLogging(logName, logParameterFunction.invoke(call)) {handleLogic(call)}

}