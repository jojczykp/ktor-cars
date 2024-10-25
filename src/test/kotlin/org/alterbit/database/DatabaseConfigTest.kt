package org.alterbit.database

import io.kotest.matchers.shouldBe
import io.ktor.server.config.*
import org.junit.jupiter.api.Test

class DatabaseConfigTest {

    @Test
    fun `should get parameters from application config`() {
        val applicationConfig = MapApplicationConfig(
            "app.database.url" to "jdbc:postgresql://example.com:5432/test",
            "app.database.user" to "test-user",
            "app.database.password" to "test-password",
            "app.database.poolName" to "test-pool-name",
            "app.database.maximumPoolSize" to "12345")

        val databaseConfig = DatabaseConfig(applicationConfig)

        databaseConfig.apply {
            url shouldBe "jdbc:postgresql://example.com:5432/test"
            user shouldBe "test-user"
            password shouldBe "test-password"
            poolName shouldBe "test-pool-name"
            maximumPoolSize shouldBe 12345
        }
    }
}
