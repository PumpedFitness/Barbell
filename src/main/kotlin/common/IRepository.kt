package ord.pumped.common

import java.util.*

interface IRepository<T, E> {
    fun save(user: T): E
    fun update(user: T): E
    fun delete(id: UUID)
    fun findByID(id: UUID): E?
}