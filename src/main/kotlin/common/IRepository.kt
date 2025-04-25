package ord.pumped.common

fun interface IRepository<T, E> {
    fun save(user: T): E
}