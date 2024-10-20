package org.alterbit.repositories

import io.ktor.server.config.*

class DatabaseConfig(config: ApplicationConfig) {
    val url = config.property("app.database.url").getString()
    val user = config.property("app.database.user").getString()
    val password = config.property("app.database.password").getString()
    val poolName = config.property("app.database.poolName").getString()
    val maximumPoolSize = config.property("app.database.maximumPoolSize").getString().toInt()
}
