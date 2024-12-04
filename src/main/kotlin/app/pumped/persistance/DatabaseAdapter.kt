package app.pumped.persistance

import com.zaxxer.hikari.HikariDataSource

/**
 * @author Devin Fritz
 */
interface DatabaseAdapter {

    fun getDataSource(): HikariDataSource

}