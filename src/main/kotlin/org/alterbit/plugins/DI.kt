package org.alterbit.plugins

import io.ktor.server.application.*
import org.alterbit.assembler.CarResponseAssembler
import org.alterbit.assembler.CreateCarAssembler
import org.alterbit.assembler.UpdateCarAssembler
import org.alterbit.repositories.CarsRepository
import org.alterbit.services.CarsService
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.kodein.di.singleton

fun Application.configureDI() {
    di {
        bind<CarsRepository>() with singleton { CarsRepository() }

        bind<CarsService>() with singleton { CarsService(instance()) }

        bind<CreateCarAssembler>() with singleton { CreateCarAssembler() }
        bind<UpdateCarAssembler>() with singleton { UpdateCarAssembler() }
        bind<CarResponseAssembler>() with singleton { CarResponseAssembler() }
    }
}
