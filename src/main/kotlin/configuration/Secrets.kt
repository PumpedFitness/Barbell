package ord.pumped.configuration

import io.ktor.server.application.Application
import io.ktor.util.AttributeKey
import ord.pumped.io.secret.SecretAdapter

fun Application.configureSecrets(testing: Boolean = false) {
    val adapterType = if (testing)
        "env"
    else
        System.getenv("BB_SECRET_ADAPTER") ?: error("BB_SECRET_ADAPTER is not defined")

    val secretAdapter = SecretAdapter.getSecretAdapter(adapterType)
    secretAdapter.validate()

    attributes.put(BBSecretAccessor, secretAdapter)
}

val BBSecretAccessor = AttributeKey<SecretAdapter>("BB_SECRETS")

private fun Application.secrets(): SecretAdapter = attributes[BBSecretAccessor]
val Application.secrets: SecretAdapter get() = secrets()