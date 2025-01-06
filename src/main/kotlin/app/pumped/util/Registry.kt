package app.pumped.util

/**
 * Models a registration for objects
 *
 * @author Devin Fritz
 */
abstract class Registry<T> {

    private val registeredObjects = mutableSetOf<T>()

    fun register(t: T) {
        registeredObjects += t
    }

    fun unregister(t: T) {
        registeredObjects += t
    }

    fun registeredObjects(): Set<T> {
        return registeredObjects.toSet()
    }

}