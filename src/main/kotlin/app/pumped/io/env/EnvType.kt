package app.pumped.io.env

/**
 * Models a datatype values can use when written in the .env
 */
enum class EnvType(val typeCheck: (value: String) -> Boolean) {
    /**
     * A String value, can be cast to
     * @see String
     */
    STRING( {true} ),

    /**
     * A long value, can be cast to
     * @see Long
     */
    LONG( { it.toLongOrNull() != null } ),

    /**
     * An int value, can be cast to
     * @see Int
     */
    INT( { it.toIntOrNull() != null } ),

    /**
     * A number, can be cast to
     * @see Double
     */
    NUMBER( { it.toDoubleOrNull() != null } ),

    /**
     * A boolean, can be strictly cast to
     * @see Boolean
     */
    BOOLEAN( { it.toBooleanStrictOrNull() != null } )
}
