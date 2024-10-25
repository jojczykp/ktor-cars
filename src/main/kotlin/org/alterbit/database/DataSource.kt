package org.alterbit.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

class DataSource(private val config: DatabaseConfig) : DataSource by
    HikariDataSource(HikariConfig().apply {
        jdbcUrl = config.url
        username = config.user
        password = config.password
        poolName = config.poolName
        maximumPoolSize = config.maximumPoolSize
    })
