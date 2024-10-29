package org.alterbit.ktorcars.plugins

import io.ktor.server.application.*
import io.ktor.server.config.*
import org.alterbit.ktorcars.database.DataSource
import org.alterbit.ktorcars.database.DatabaseConfig
import org.alterbit.ktorcars.database.cars.CarsIdGenerator
import org.alterbit.ktorcars.services.CarsService
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
        bind<org.alterbit.ktorcars.database.cars.CarsDao>() with singleton { instance<Jdbi>().onDemand(org.alterbit.ktorcars.database.cars.CarsDao::class.java) }

        bind<CarsIdGenerator>() with singleton { CarsIdGenerator() }
        bind<CarsService>() with singleton { CarsService(instance(), instance()) }

        bind<org.alterbit.ktorcars.rest.cars.CarsConverter>() with singleton { org.alterbit.ktorcars.rest.cars.CarsConverter() }
    }
}
