package ord.pumped.util

import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty

/**
 * Example usage:
 * "private val log by LoggerDelegate()"
 */
class LoggerDelegate {
    operator fun getValue(thisRef: Any, property: KProperty<*>): Logger {
        return LoggerFactory.getLogger(thisRef.javaClass)
    }
}

/**
 * There are two ways to use this:
 * 1. In a normal class you can just use something like this:
 *      withLogging("MyOperation", "param1" to "value1", "param2" to "value2") {
 *          // Your code here
 *      }
 * 2. In an interface if you want to force logging, you can make a logic function and wrap it like this:
 *          fun saveLogic(entity: T): E
 *          fun save(entity: T): E = withLogging("save", "entity" to entity) {saveLogic(entity)}
 * If you use it in an interface, you should use this template with logic function to stay consistent.
 */
interface Loggable {
    val log: Logger get() = LoggerFactory.getLogger(this::class.java)
    fun <T> withLogging(
        operationName: String,
        vararg params: Pair<String, Any?>,
        block: suspend () -> T
    ): T {
        val paramStr = params.joinToString(", ") { (key, value) -> "$key=${value.toString()}" }
        log.info("\'$operationName\' with parameters: $paramStr")
        return try {
            val result = runBlocking { block() }
            result
        } catch (e: Exception) {
            log.error("Exception while \'$operationName\' with parameters: $paramStr", e)
            throw e
        }
    }
}