package app.pumped.domain

/**
 * Models a repository to access the database from the given value T over the primary key P
 *
 * @param P primary key
 * @param T Entity
 *
 * @author Devin Fritz
 */
interface Repository<P, E> {

    /**
     * @return all entities saved to the table
     */
    fun getAll(): List<E>

    /**
     * @param key of the entity
     * @return the entity, null if not found
     */
    fun get(key: P): E?

    /**
     * inserts the given entity into the table
     * @param entity
     */
    fun insert(entity: E)

    /**
     * Deletes the entity
     * @param entity
     */
    fun delete(entity: E)

    /**
     * deletes the entity with the given key
     * @param key
     */
    fun deleteById(key: P)

    /**
     * updates the given entity
     * @param entity
     */
    fun update(entity: E)

}