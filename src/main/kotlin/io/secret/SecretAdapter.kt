package ord.pumped.io.secret

import ord.pumped.io.env.EnvAccessor
import ord.pumped.io.env.EnvVariables
import ord.pumped.io.secret.adapters.DotEnvAdapter
import ord.pumped.io.secret.adapters.SystemEnvAdapter
import ord.pumped.io.secret.adapters.VaultAdapter

interface SecretAdapter: EnvAccessor {

    companion object {
        fun getSecretAdapter(type: String): SecretAdapter {
            return when (type) {
                "env" -> DotEnvAdapter()
                "system" -> SystemEnvAdapter()
                "vault" -> VaultAdapter()
                else -> error("Unsupported type: $type")
            }
        }
    }

    fun get(key: String): String?

    override operator fun get(variable: EnvVariables) = get(variable.name)!!

    fun getAllKeys(): Set<String>

    fun getAll(): Map<String, String>

    fun validate(): Boolean {
        val keys = getAllKeys()
        val envVariables = EnvVariables.entries

        for (envVariable in envVariables) {
            if (!keys.contains(envVariable.name)) {
                error("Missing required environment variable ${envVariable.name}")
            }

            val value = get(envVariable.name)

            if (value == null) {
                error("Missing required environment variable ${envVariable.name}")
            }

            if (!envVariable.type.cast(value)) {
                error("Failed to cast ${envVariable.name} to ${envVariable.type.cast(value)}")
            }

            if (envVariable.requiresNonEmpty && value.isEmpty()) {
                error("Variable ${envVariable.name} requires non empty value, but a empty value has been supplied!")
            }
        }

        return true
    }
}