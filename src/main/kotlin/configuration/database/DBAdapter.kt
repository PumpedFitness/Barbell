package ord.pumped.configuration.database

import io.ktor.server.application.*
import javax.sql.DataSource

fun interface DBAdapter {

    fun asDataSource(application: Application): DataSource

}