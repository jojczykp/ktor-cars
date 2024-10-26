package org.alterbit.services

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.alterbit.database.CarsDao
import org.alterbit.database.CarsIdGenerator
import org.alterbit.commands.CreateCarCommand
import org.alterbit.commands.UpdateCarCommand
import org.alterbit.exceptions.CarNotFoundException
import org.alterbit.exceptions.CreateCarException
import org.alterbit.exceptions.UpdateCarException
import org.alterbit.model.Car

class CarsServiceTest : ShouldSpec({

    val id = "88046956-0f4c-41a4-876a-f6cccf66b475"
    val make = "Škoda"
    val colour = "Blue"

    val car = Car(id, make, colour)

    val exception = Exception("error")

    val cars = setOf(
        Car("0684c5f5-cc63-426e-a86d-da3f7e6521ad", "Audi", "Red"),
        Car("ab343f4d-101f-4c75-985f-601f648336c9", "BMW", "Blue"),
        Car("07471545-ce58-4ea7-8160-79f89b15c1d4", "Lexus", "Pink")
    )

    val dao = mockk<CarsDao>()
    val carsIdGenerator = mockk<CarsIdGenerator>()

    val service = CarsService(dao, carsIdGenerator)

    beforeEach{
        clearAllMocks()
    }

    context("create car") {

        should("create car and return it") {
            val command = CreateCarCommand(make, colour)
            every { carsIdGenerator.newId() } returns id
            every { dao.createCar(id, command) } returns true
            every { dao.getCar(id) } returns car

            val result = service.createCar(command)

            verify(exactly = 1) { carsIdGenerator.newId() }
            verify(exactly = 1) { dao.createCar(id, command) }
            verify(exactly = 1) { dao.getCar(id) }
            result shouldBeSuccess car
        }

        should("fail create car if dao failed on creation") {
            val command = CreateCarCommand(make, colour)
            every { carsIdGenerator.newId() } returns id
            every { dao.createCar(id, command) } returns false

            val result = service.createCar(command)

            verify(exactly = 1) { carsIdGenerator.newId() }
            verify(exactly = 1) { dao.createCar(id, command) }
            verify(exactly = 0) { dao.getCar(any()) }
            result.shouldBeFailure {
                it.shouldBeInstanceOf<CreateCarException>()
                it.id shouldBe id
            }
        }

        should("capture exception if creation failed") {
            val command = CreateCarCommand(make, colour)
            every { carsIdGenerator.newId() } returns id
            every { dao.createCar(id, command) } throws exception

            val result = service.createCar(command)

            verify(exactly = 1) { carsIdGenerator.newId() }
            verify(exactly = 1) { dao.createCar(id, command) }
            verify(exactly = 0) { dao.getCar(any()) }
            result shouldBeFailure exception
        }

        should("capture exception if fetch of created car failed") {
            val command = CreateCarCommand(make, colour)
            every { carsIdGenerator.newId() } returns id
            every { dao.createCar(id, command) } returns true
            every { dao.getCar(id) } throws exception

            val result = service.createCar(command)

            verify(exactly = 1) { carsIdGenerator.newId() }
            verify(exactly = 1) { dao.createCar(id, command) }
            verify(exactly = 1) { dao.getCar(id) }
            result shouldBeFailure exception
        }
    }

    context("getCars") {

        should("return cars") {
            every { dao.getCars() } returns cars

            val result = service.getCars()

            verify { carsIdGenerator wasNot called }
            result shouldBe cars
        }
    }

    context("getCar") {

        should("return car by id") {
            every { dao.getCar(id) } returns car

            val result = service.getCar(id)

            verify { carsIdGenerator wasNot called }
            result shouldBeSuccess car
        }

        should("fail if car has not been found") {
            every { dao.getCar(id) } returns null

            val result = service.getCar(id)

            verify { carsIdGenerator wasNot called }
            result shouldBeFailure {
                it.shouldBeInstanceOf<CarNotFoundException>()
                it.id shouldBe id
            }
        }
    }

    context("update car") {

        should("update car and return it's updated version") {
            val command = UpdateCarCommand(id, make, colour)
            every { dao.updateCar(command) } returns true
            every { dao.getCar(id) } returns car

            val result = service.updateCar(command)

            verify(exactly = 1) { dao.updateCar(command) }
            verify(exactly = 1) { dao.getCar(id) }
            verify { carsIdGenerator wasNot called }
            result shouldBeSuccess car
        }

        should("fail updating car that has not been found") {
            val command = UpdateCarCommand(id, make, colour)
            every { dao.updateCar(command) } returns false

            val result = service.updateCar(command)

            verify(exactly = 1) { dao.updateCar(command) }
            verify(exactly = 0) { dao.getCar(any()) }
            verify { carsIdGenerator wasNot called }
            result shouldBeFailure {
                it.shouldBeInstanceOf<UpdateCarException>()
                it.id shouldBe id
            }
        }

        should("capture exception if update failed") {
            val command = UpdateCarCommand(id, make, colour)
            every { dao.updateCar(command) } throws exception

            val result = service.updateCar(command)

            verify(exactly = 1) { dao.updateCar(command) }
            verify(exactly = 0) { dao.getCar(id) }
            verify { carsIdGenerator wasNot called }
            result shouldBeFailure exception
        }

        should("capture exception if fetch of updated car failed") {
            val command = UpdateCarCommand(id, make, colour)
            every { dao.updateCar(command) } returns true
            every { dao.getCar(id) } throws exception

            val result = service.updateCar(command)

            verify(exactly = 1) { dao.updateCar(command) }
            verify(exactly = 1) { dao.getCar(id) }
            verify { carsIdGenerator wasNot called }
            result shouldBeFailure exception
        }
    }

    context("deleteCar") {

        should("delete car by id") {
            every { dao.deleteCar(id) } returns true

            val result = service.deleteCar(id)

            verify(exactly = 1) { dao.deleteCar(id) }
            verify { carsIdGenerator wasNot called }
            result shouldBeSuccess true
        }

        should("not delete car if car has not been found") {
            every { dao.deleteCar(id) } returns false

            val result = service.deleteCar(id)

            verify(exactly = 1) { dao.deleteCar(id) }
            verify { carsIdGenerator wasNot called }
            result shouldBeSuccess false
        }

        should("capture exception if deletion failed") {
            every { dao.deleteCar(id) } throws exception

            val result = service.deleteCar(id)

            verify(exactly = 1) { dao.deleteCar(id) }
            verify { carsIdGenerator wasNot called }
            result shouldBeFailure exception
        }
    }
})
