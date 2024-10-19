package org.alterbit.repositories

import io.ktor.server.config.*
import java.sql.Connection
import java.sql.DriverManager

class DatabaseConnectionFactory(private val applicationConfig: ApplicationConfig) {
    fun createConnection(): Connection =
        DriverManager.getConnection(
            applicationConfig.property("cars.database.url").getString(),
            applicationConfig.property("cars.database.user").getString(),
            applicationConfig.property("cars.database.password").getString()
        ).apply {
            autoCommit = true
        }.apply {
            Runtime.getRuntime().addShutdownHook(Thread { close() })
        }
}
