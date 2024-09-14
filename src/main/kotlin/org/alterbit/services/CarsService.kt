package org.alterbit.services

import org.alterbit.repositories.CarsRepository
import org.alterbit.model.Car

class CarsService(private val carsRepository: CarsRepository) {
    fun getCars(): List<Car> = carsRepository.getCars()
}
