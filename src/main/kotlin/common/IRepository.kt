package ord.pumped.common

import ord.pumped.util.Loggable
import java.util.*

interface IRepository<T, E>: Loggable {
    fun saveLogic(entity: T): E
    fun updateLogic(entity: T): E
    fun deleteLogic(id: UUID)
    fun findByIDLogic(id: UUID): E?

    fun save(entity: T): E = withLogging("save", "entity" to entity) {
        saveLogic(entity)
    }

    fun update(entity: T): E = withLogging("update", "entity" to entity) {
        updateLogic(entity)
    }

    fun delete(id: UUID) = withLogging("delete", "id" to id) {
        deleteLogic(id)
    }

    fun findByID(id: UUID): E? = withLogging("findByID", "id" to id) {
        findByIDLogic(id)
    }
}