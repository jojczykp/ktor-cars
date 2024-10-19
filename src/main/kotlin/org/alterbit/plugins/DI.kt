package org.alterbit.plugins

import io.ktor.server.application.*
import io.ktor.server.config.*
import org.alterbit.assembler.CarResponseAssembler
import org.alterbit.assembler.CreateCarAssembler
import org.alterbit.assembler.UpdateCarAssembler
import org.alterbit.repositories.CarsRepository
import org.alterbit.repositories.DatabaseConnectionFactory
import org.alterbit.services.CarsService
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import java.sql.Connection

fun Application.configureDI() {
    di {
        bind<ApplicationConfig>() with singleton { environment.config }

        bind<Connection>() with singleton { DatabaseConnectionFactory(instance()).createConnection() }

        bind<CarsRepository>() with singleton { CarsRepository(instance()) }

        bind<CarsService>() with singleton { CarsService(instance()) }

        bind<CreateCarAssembler>() with singleton { CreateCarAssembler() }
        bind<UpdateCarAssembler>() with singleton { UpdateCarAssembler() }
        bind<CarResponseAssembler>() with singleton { CarResponseAssembler() }
    }
}
