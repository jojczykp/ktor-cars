package org.alterbit.services

import org.alterbit.commands.CreateCarCommand
import org.alterbit.commands.UpdateCarCommand
import org.alterbit.model.Car
import org.alterbit.database.cars.CarsDao
import org.alterbit.database.cars.CarsIdGenerator
import org.alterbit.exceptions.CreateCarException
import org.alterbit.exceptions.CarNotFoundException
import org.alterbit.exceptions.UpdateCarException

class CarsService(
    private val carsDao: CarsDao,
    private val carsIdGenerator: CarsIdGenerator
) {

    fun getCars(): Set<Car> = carsDao.getCars()

    fun getCar(id: String): Result<Car> = runCatching {
        carsDao.getCar(id) ?: throw CarNotFoundException(id)
    }

    fun createCar(command: CreateCarCommand): Result<Car> = runCatching {
        val id = carsIdGenerator.newId()
        if (carsDao.createCar(id, command)) {
            carsDao.getCar(id) ?: throw CarNotFoundException(id)
        } else {
            throw CreateCarException(id)
        }
    }

    fun deleteCar(id: String): Result<Boolean> = runCatching {
        carsDao.deleteCar(id)
    }

    fun updateCar(command: UpdateCarCommand): Result<Car> = runCatching {
        if (carsDao.updateCar(command)) {
            carsDao.getCar(command.id) ?: throw CarNotFoundException(command.id)
        } else {
            throw UpdateCarException(command.id)
        }
    }
}
