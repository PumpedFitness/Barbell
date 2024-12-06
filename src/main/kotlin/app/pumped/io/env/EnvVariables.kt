package app.pumped.io.env

enum class EnvVariables(
    val type: EnvType,
    val default: String? = null,
    val requiresNonEmpty: Boolean = true,
    val isHidden: Boolean = true,
) {

    BB_PROGRAM_SCOPE(EnvType.STRING),

    BB_DB_USER(EnvType.STRING),
    BB_DB_PASSWORD(EnvType.STRING),
    BB_DB_HOST(EnvType.STRING),
    BB_DB_PORT(EnvType.INT),
    BB_DB_DATABASE(EnvType.STRING),

    BB_REDIS_HOST(EnvType.STRING),
    BB_REDIS_PORT(EnvType.INT),
    BB_REDIS_PASSWORD(EnvType.STRING)

}