package org.alterbit.ktorcars.services

import org.alterbit.ktorcars.commands.CreateCarCommand
import org.alterbit.ktorcars.commands.UpdateCarCommand
import org.alterbit.ktorcars.model.Car
import org.alterbit.ktorcars.database.cars.CarsIdGenerator
import org.alterbit.ktorcars.exceptions.CreateCarException
import org.alterbit.ktorcars.exceptions.CarNotFoundException

class CarsService(
    private val carsDao: org.alterbit.ktorcars.database.cars.CarsDao,
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
