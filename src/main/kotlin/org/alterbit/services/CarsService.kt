package org.alterbit.services

import org.alterbit.dto.CreateCarCommand
import org.alterbit.dto.UpdateCarCommand
import org.alterbit.model.Car
import org.alterbit.database.CarsDao
import java.util.*

class CarsService(private val carsDao: CarsDao) {

    fun getCars(): Set<Car> = carsDao.getCars()

    fun getCar(id: String): Result<Car> = runCatching {
        carsDao.getCar(id) ?: throw IllegalArgumentException("Car with id $id not found")
    }

    fun createCar(command: CreateCarCommand): Result<Car> = runCatching {
        val id = UUID.randomUUID().toString()
        carsDao.createCar(id, command)
        carsDao.getCar(id) ?: throw IllegalArgumentException("Car with id $id not found")
    }

    fun deleteCar(id: String): Result<Boolean> = runCatching {
        carsDao.deleteCar(id)
    }

    fun updateCar(command: UpdateCarCommand): Result<Car> = runCatching {
        if (carsDao.updateCar(command)) {
            carsDao.getCar(command.id) ?: throw IllegalArgumentException("Car with id ${command.id} not found")
        } else {
            throw IllegalArgumentException("Car with id ${command.id} not found")
        }
    }
}
