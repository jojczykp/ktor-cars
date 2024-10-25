package org.alterbit.plugins

import io.ktor.server.application.*
import org.alterbit.database.DatabaseConfig
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
