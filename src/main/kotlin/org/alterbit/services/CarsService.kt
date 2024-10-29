package org.alterbit.services

import org.alterbit.commands.CreateCarCommand
import org.alterbit.commands.UpdateCarCommand
import org.alterbit.model.Car
import org.alterbit.database.cars.CarsDao
import org.alterbit.database.cars.CarsIdGenerator
import org.alterbit.exceptions.CreateCarException
import org.alterbit.exceptions.CarNotFoundException

class CarsService(
    private val carsDao: CarsDao,
    private val carsIdGenerator: CarsIdGenerator
) {
    fun getCars(): Set<Car> = carsDao.getCars()

    fun getCar(id: String): Car? = carsDao.getCar(id)

    fun createCar(command: CreateCarCommand): Car {
        val id = carsIdGenerator.newId()
        carsDao.createCar(id, command) || throw CreateCarException(id)
        val createdCar = carsDao.getCar(id) ?: throw CarNotFoundException(id)

        return createdCar
    }

    fun deleteCar(id: String): Car? {
        val deletedCar = carsDao.getCar(id) ?: return null
        carsDao.deleteCar(id)

        return deletedCar
    }

    fun updateCar(command: UpdateCarCommand): Car? {
        carsDao.updateCar(command) || return null
        val updatedCar = carsDao.getCar(command.id) ?: throw CarNotFoundException(command.id)

        return updatedCar
    }
}
