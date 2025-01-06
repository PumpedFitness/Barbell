package app.pumped.data.persistance

import com.zaxxer.hikari.HikariDataSource

/**
 * @author Devin Fritz
 */
interface DatabaseAdapter {

    fun getDataSource(): HikariDataSource

}