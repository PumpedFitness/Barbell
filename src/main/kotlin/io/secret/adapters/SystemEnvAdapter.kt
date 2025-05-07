package ord.pumped.io.secret.adapters

import ord.pumped.io.secret.SecretAdapter

class SystemEnvAdapter(): SecretAdapter {

    override fun get(key: String): String? {
        return System.getProperty(key) ?: null
    }

    override fun getAllKeys(): Set<String> {
        return System.getenv().map { it.key }.toSet()
    }

    override fun getAll(): Map<String, String> {
        return System.getenv()
    }
}