package app.pumped.persistance.adapter

import app.pumped.persistance.DatabaseAdapter
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

/**
 * @author Devin Fritz
 */
class PostgreAdapter: DatabaseAdapter {

    override fun getDataSource(): HikariDataSource {
        val config = HikariConfig().apply {

        }
        return HikariDataSource(config)
    }
}