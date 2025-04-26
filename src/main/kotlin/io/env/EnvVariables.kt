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
}

enum class EnvType {
    STRING,
    LONG,
    INT,
    NUMBER,
    BOOLEAN,
}