package app.pumped.io.env

enum class EnvVariables(
    val type: EnvType,
    val default: String? = null,
    val requiresNonEmpty: Boolean = true,
    val isHidden: Boolean = true,
) {

    BB_PROGRAM_SCOPE(EnvType.STRING)

}