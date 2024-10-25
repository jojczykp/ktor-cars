package org.alterbit.plugins

import io.ktor.server.application.*
import io.ktor.server.config.*
import org.alterbit.assembler.CarResponseAssembler
import org.alterbit.assembler.CreateCarAssembler
import org.alterbit.assembler.UpdateCarAssembler
import org.alterbit.database.CarsDao
import org.alterbit.database.DataSource
import org.alterbit.database.DatabaseConfig
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

        bind<CarsService>() with singleton { CarsService(instance()) }

        bind<CreateCarAssembler>() with singleton { CreateCarAssembler() }
        bind<UpdateCarAssembler>() with singleton { UpdateCarAssembler() }
        bind<CarResponseAssembler>() with singleton { CarResponseAssembler() }
    }
}
