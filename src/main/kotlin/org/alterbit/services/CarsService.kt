package org.alterbit.services

import org.alterbit.repositories.CarsRepository
import org.alterbit.model.Car

class CarsService(private val carsRepository: CarsRepository) {

    fun getCars(): List<Car> = carsRepository.getCars()

    fun getCar(id: Int): Result<Car> = carsRepository.getCar(id)

    fun createCar(make: String): Result<Car> = carsRepository.createCar(make)
}
