package org.alterbit.services

import org.alterbit.dto.UpdateCarCommand
import org.alterbit.repositories.CarsRepository
import org.alterbit.model.Car

class CarsService(private val carsRepository: CarsRepository) {

    fun getCars(): Set<Car> = carsRepository.getCars()

    fun getCar(id: Int): Result<Car> = carsRepository.getCar(id)

    fun createCar(make: String, colour: String): Result<Car> = carsRepository.createCar(make, colour)

    fun deleteCar(id: Int): Result<Boolean> = carsRepository.deleteCar(id)

    fun updateCar(command: UpdateCarCommand): Result<Car> = carsRepository.updateCar(command)
}
