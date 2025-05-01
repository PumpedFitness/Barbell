package ord.pumped.common

interface IRepository<T, E> {
    fun save(user: T): E
    fun update(user: T): E
}