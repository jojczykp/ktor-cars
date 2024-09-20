package org.alterbit

import org.alterbit.assembler.CarResponseAssembler
import org.alterbit.assembler.CreateCarAssembler
import org.alterbit.assembler.UpdateCarAssembler
import org.alterbit.repositories.CarsRepository
import org.alterbit.services.CarsService
import org.koin.dsl.module

val carsModule = module {
    single { CarsRepository() }

    single { CarsService(get()) }

    single { CreateCarAssembler() }
    single { UpdateCarAssembler() }
    single { CarResponseAssembler() }
}
