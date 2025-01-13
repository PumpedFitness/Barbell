package app.pumped.io.env

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.util.logging.*

val mode: ProgramScope = ProgramScope.valueOf(System.getenv(EnvVariables.BB_PROGRAM_SCOPE.toString()) ?: "DEV")

/**
 * Util class to access values from the provided environment.
 *
 * Will load from environment variables, or a .env file depending on the provided ProgrammScope
 * if [ProgramScope.DEV] is provided, values will be loaded from the .env file
 * if [ProgramScope.DEPLOY] is provided, values will be loaded from the environment variables
 *
 * [EnvVariables.BB_PROGRAM_SCOPE] must be provided by environment variables, to set the scope correctly
 *
 * To add more entries to load from, adjust [EnvVariables]
 */
class Env(
    private val logger: Logger,
) {
    private var dotenv: Dotenv? = null

    init {
        if (mode == ProgramScope.DEV) {
            dotenv = dotenv()
        }

        val entries = EnvVariables.entries
        logger.debug("${entries.size} env entries were supplied")

        val setEntries = getAllKeys()

        if (setEntries.size > entries.size) {
            logger.warn("${setEntries.size} were entries found, but only ${entries.size} is required")

            for (setEntry in setEntries) {
                if (entries.firstOrNull { it.name == setEntry } == null) {
                    logger.warn("$setEntry is set but not required by local definitions!")
                }
            }
        }

        for (value in entries) {
            val envValue =
                value.default
                    ?: (getOrNull(value.name) ?: error("Key ${value.name} not found in environment"))

            if (!value.type.typeCheck(envValue)) {
                error("Can't cast ${value.name} to ${value.type}")
            }

            if (value.requiresNonEmpty && envValue.isEmpty()) {
                error("Value ${value.name} requires non empty value, but a empty value has been supplied!")
            }
        }
    }

    private fun getAllKeys(): List<String> =
        if (mode == ProgramScope.DEV) {
            dotenv!!.entries().map { it.key }
        } else {
            System.getenv().map { it.key }
        }

    private fun getOrNull(s: String): String? {
        return if (mode == ProgramScope.DEV) {
            if (dotenv!!.entries().none { it.key == s }) {
                return null
            }

            dotenv!!.get(s)
        } else {
            if (System.getenv(s) == null) {
                return null
            }
            System.getenv(s)
        }
    }

    operator fun get(variable: EnvVariables): String {
        return getOrNull(variable.name) ?: run {
            if (variable.default != null) {
                return variable.default
            }
            error("${variable.name} no found in .env")
        }
    }
}

/**
 * Models the scope the backend is executed im
 */
enum class ProgramScope {
    DEPLOY,
    DEV,
}
