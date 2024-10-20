package org.alterbit.utils

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.MountableFile
import javax.sql.DataSource

class PostgreSQLExtension : BeforeAllCallback, AfterAllCallback {

    private lateinit var database: PostgreSQLContainer<*>

    lateinit var url: String
    lateinit var user: String
    lateinit var password: String

    lateinit var dataSource: DataSource

    override fun beforeAll(context: ExtensionContext?) {
        database = PostgreSQLContainer("postgres:16-alpine")
            .withCopyFileToContainer(
                MountableFile.forClasspathResource("init.sql"),
                "/docker-entrypoint-initdb.d/init.sql")

        database.start()

        url = database.jdbcUrl
        user = database.username
        password = database.password

        dataSource = HikariDataSource(HikariConfig().apply {
            jdbcUrl = database.jdbcUrl
            username = database.username
            password = database.password
            poolName = javaClass.simpleName
            maximumPoolSize = 3
        })
    }

    override fun afterAll(context: ExtensionContext?) {
        (dataSource as HikariDataSource).close()
        database.stop()
    }
}
