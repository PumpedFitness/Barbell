package ord.pumped.io.env

fun interface EnvAccessor {

    /**
     * Reads the given variable from the environment and tries to parse it to the given type
     * @see EnvVariables
     * @param variable the variable to read
     * @return the value read
     */
    operator fun get(variable: EnvVariables): String
}