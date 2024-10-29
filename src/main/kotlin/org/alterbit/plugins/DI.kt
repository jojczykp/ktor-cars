package org.alterbit.plugins

import io.ktor.server.application.*
import io.ktor.server.config.*
import org.alterbit.rest.cars.CarsConverter
import org.alterbit.database.cars.CarsDao
import org.alterbit.database.DataSource
import org.alterbit.database.DatabaseConfig
import org.alterbit.database.cars.CarsIdGenerator
import org.alterbit.services.CarsService
import org.jdbi.v3.core.Jdbi
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.kodein.di.singleton

fun Application.configureDI() {
    di {
        bind<ApplicationConfig>() with singleton { environment.config }
        bind<DatabaseConfig>() with singleton { DatabaseConfig(instance()) }

        bind<DataSource>() with singleton { DataSource(instance()) }
        bind<Jdbi>() with singleton { Jdbi.create(instance<javax.sql.DataSource>()).installPlugins() }
        bind<CarsDao>() with singleton { instance<Jdbi>().onDemand(CarsDao::class.java) }

        bind<CarsIdGenerator>() with singleton { CarsIdGenerator() }
        bind<CarsService>() with singleton { CarsService(instance(), instance()) }

        bind<CarsConverter>() with singleton { CarsConverter() }
    }
}
