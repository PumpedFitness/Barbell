package ord.pumped.io.secret.adapters

import io.github.hansanto.kault.VaultClient
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive
import ord.pumped.io.secret.SecretAdapter

class VaultAdapter(): SecretAdapter {

    private val vault = VaultClient {
        url = System.getenv("BB_VAULT_HOST") ?: "http://localhost:8200"
        auth {
            setTokenString(System.getenv("BB_VAULT_TOKEN") ?: error("Vault token is not defined"))
        }
    }

    private val path = System.getenv("BB_VAULT_PATH")
    private val kv2 = vault.secret.kv2

    private val secrets: Map<String, JsonElement> = runBlocking {
        kv2.readSecret(path).data?.toMap() ?: emptyMap()
    }

    override fun get(key: String): String? {
        return secrets[key]?.jsonPrimitive?.content
    }

    override fun getAllKeys(): Set<String> {
        return secrets.keys
    }

    override fun getAll(): Map<String, String> {
        return secrets.mapValues { it.value.toString() }
    }
}