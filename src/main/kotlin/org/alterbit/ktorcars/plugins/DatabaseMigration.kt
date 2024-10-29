package org.alterbit.ktorcars.plugins

import io.ktor.server.application.*
import org.alterbit.ktorcars.database.DatabaseConfig
import org.flywaydb.core.Flyway

fun Application.migrateDatabase() {
    val databaseConfig = DatabaseConfig(environment.config)

    val flyway = Flyway.configure()
        .dataSource(
            databaseConfig.url,
            databaseConfig.user,
            databaseConfig.password)
        .load()

    flyway.migrate()
}
