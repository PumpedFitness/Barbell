package ord.pumped.io.secret.adapters

import io.github.cdimascio.dotenv.dotenv
import ord.pumped.io.secret.SecretAdapter

class DotEnvAdapter(): SecretAdapter {
    private val dotenv = dotenv()

    override fun get(key: String): String? {
        if (!dotenv.entries().any { it.key == key }) return null
        return dotenv[key]
    }

    override fun getAllKeys(): Set<String> {
        return dotenv.entries().map { it.key }.toSet()
    }

    override fun getAll(): Map<String, String> {
        return dotenv.entries().associate { it.key to it.value }
    }
}