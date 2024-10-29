package org.alterbit.ktorcars.services

import org.alterbit.ktorcars.commands.CreateCarCommand
import org.alterbit.ktorcars.commands.UpdateCarCommand
import org.alterbit.ktorcars.model.Car
import org.alterbit.ktorcars.database.cars.CarsIdGenerator
import org.alterbit.ktorcars.exceptions.CreateCarException
import org.alterbit.ktorcars.exceptions.CarNotFoundException
import org.slf4j.LoggerFactory

class CarsService(
    private val carsDao: org.alterbit.ktorcars.database.cars.CarsDao,
    private val carsIdGenerator: CarsIdGenerator
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getCars(): Set<Car> {
        logger.info("Getting all cars")

        return carsDao.getCars()
    }

    fun getCar(id: String): Car? {
        logger.info("Getting car with id <{}>", id)

        return carsDao.getCar(id)
    }

    fun createCar(command: CreateCarCommand): Car {
        logger.info("Getting car")

        val id = carsIdGenerator.newId()
        carsDao.createCar(id, command) || throw CreateCarException(id)
        val createdCar = carsDao.getCar(id) ?: throw CarNotFoundException(id)

        return createdCar
    }

    fun deleteCar(id: String): Car? {
        logger.info("Deleting car with id <{}>", id)

        val deletedCar = carsDao.getCar(id) ?: return null
        carsDao.deleteCar(id)

        return deletedCar
    }

    fun updateCar(command: UpdateCarCommand): Car? {
        logger.info("Updating car with id <{}>", command.id)

        carsDao.updateCar(command) || return null
        val updatedCar = carsDao.getCar(command.id) ?: throw CarNotFoundException(command.id)

        return updatedCar
    }
}
