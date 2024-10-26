package org.alterbit.test_utils

import com.zaxxer.hikari.HikariDataSource
import io.kotest.core.extensions.MountableExtension
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.AfterTestListener
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.listeners.BeforeTestListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.extensions.testcontainers.JdbcDatabaseContainerExtension
import io.kotest.extensions.testcontainers.toDataSource
import org.flywaydb.core.Flyway
import org.testcontainers.containers.JdbcDatabaseContainer

class FlywayDBContainerExtension(private val database: JdbcDatabaseContainer<*>) :
    MountableExtension<HikariDataSource, HikariDataSource>,
    AfterProjectListener,
    BeforeTestListener,
    BeforeSpecListener,
    AfterTestListener,
    AfterSpecListener
{
    private val delegate = JdbcDatabaseContainerExtension(
        container = database,
        afterStart = {
            database.toDataSource().use { migrationDataSource ->
                Flyway.configure().dataSource(migrationDataSource).load().migrate()
            }
        }
    )

    val jdbcUrl get() = database.jdbcUrl
    val username get() = database.username
    val password get() = database.password

    override fun mount(configure: HikariDataSource.() -> Unit) = delegate.mount(configure)
    override suspend fun afterProject() = delegate.afterProject()
    override suspend fun beforeAny(testCase: TestCase) = delegate.beforeTest(testCase)
    override suspend fun beforeTest(testCase: TestCase) = delegate.beforeTest(testCase)
    override suspend fun beforeSpec(spec: Spec) = delegate.beforeSpec(spec)
    override suspend fun afterAny(testCase: TestCase, result: TestResult) = delegate.afterAny(testCase, result)
    override suspend fun afterTest(testCase: TestCase, result: TestResult) = delegate.afterTest(testCase, result)
    override suspend fun afterSpec(spec: Spec) = delegate.afterSpec(spec)
}
