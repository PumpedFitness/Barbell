package ord.pumped.io.env

/**
 * Register all env variables inside of this class to make sure, they are loaded correctly
 */
enum class EnvVariables(
    val type: EnvType,
    val default: String? = null,
    val requiresNonEmpty: Boolean = true,
) {
    BB_MODE(EnvType.STRING, "DEV"),

    BB_DB_TYPE(EnvType.STRING),
    BB_DB_HOST(EnvType.STRING),
    BB_DB_PORT(EnvType.INT),
    BB_DB_DATABASE(EnvType.STRING),
    BB_DB_USER(EnvType.STRING),
    BB_DB_PASSWORD(EnvType.STRING),

    BB_REDIS_HOST(EnvType.STRING),
    BB_REDIS_PORT(EnvType.STRING),

    BB_JWT_SECRET(EnvType.STRING),
    BB_JWT_AUDIENCE(EnvType.STRING),
    BB_JWT_REALM(EnvType.STRING),
    BB_JWT_DOMAIN(EnvType.STRING),
    BB_JWT_EXPIRY(EnvType.NUMBER),

    BB_RABBITMQ_USER(EnvType.STRING),
    BB_RABBITMQ_PASSWORD(EnvType.STRING),
    BB_RABBITMQ_PORT(EnvType.NUMBER),
    BB_RABBITMQ_HOST(EnvType.STRING),
}

enum class EnvType(val cast: (value: String) -> Boolean) {
    STRING({ true }),
    LONG({ it.toLongOrNull() != null }),
    INT({ it.toIntOrNull() != null }),
    NUMBER({ it.toDoubleOrNull() != null }),
    BOOLEAN({ it.toBooleanStrictOrNull() != null }),
}