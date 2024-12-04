package app.pumped.persistance

import org.jetbrains.exposed.sql.Database

fun connectToDb(adapter: DatabaseAdapter): Boolean {
    val result = Database.connect(adapter.getDataSource())
    return true
}