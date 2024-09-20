package org.alterbit.services

import org.alterbit.dto.CreateCarCommand
import org.alterbit.dto.UpdateCarCommand
import org.alterbit.model.Car
import org.alterbit.repositories.CarsRepository

class CarsService(private val carsRepository: CarsRepository) {

    fun getCars(): Set<Car> = carsRepository.getCars()

    fun getCar(id: Int): Result<Car> = carsRepository.getCar(id)

    fun createCar(command: CreateCarCommand): Result<Car> = carsRepository.createCar(command)

    fun deleteCar(id: Int): Result<Boolean> = carsRepository.deleteCar(id)

    fun updateCar(command: UpdateCarCommand): Result<Car> = carsRepository.updateCar(command)
}
