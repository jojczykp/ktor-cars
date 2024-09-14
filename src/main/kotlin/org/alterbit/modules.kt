package org.alterbit

import org.alterbit.repositories.CarsRepository
import org.alterbit.services.CarsService
import org.koin.dsl.module

val carsModule = module {
    single { CarsRepository() }
    single { CarsService(get()) }
}
