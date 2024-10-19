package org.alterbit.utils

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.MountableFile
import java.sql.Connection
import java.sql.DriverManager

class PostgreSQLExtension(private val autoCommit: Boolean) : BeforeAllCallback, AfterAllCallback, AfterEachCallback {

    private lateinit var database: PostgreSQLContainer<*>

    lateinit var connection: Connection

    override fun beforeAll(context: ExtensionContext?) {
        database = PostgreSQLContainer("postgres:16-alpine")
            .withCopyFileToContainer(
                MountableFile.forClasspathResource("init.sql"),
                "/docker-entrypoint-initdb.d/init.sql")

        database.start()

        connection = database.run { DriverManager.getConnection(jdbcUrl, username, password) }
            .apply { autoCommit = this@PostgreSQLExtension.autoCommit }
    }

    override fun afterAll(context: ExtensionContext?) {
        connection.close()
        database.stop()
    }

    override fun afterEach(context: ExtensionContext?) {
        if (!autoCommit) {
            connection.rollback()
        }
    }
}
