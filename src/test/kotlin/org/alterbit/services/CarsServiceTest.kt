package org.alterbit.services

import io.kotest.matchers.shouldBe
import io.ktor.server.plugins.*
import io.mockk.every
import io.mockk.mockk
import org.alterbit.dto.CreateCarCommand
import org.alterbit.dto.UpdateCarCommand
import org.alterbit.model.Car
import org.alterbit.repositories.CarsRepository
import org.junit.jupiter.api.Test

class CarsServiceTest {

    private val repository = mockk<CarsRepository>()

    private val service = CarsService(repository)

    @Test
    fun `getCars should return cars`() {
        val expectedCars = setOf(Car(1, "Porsche", "Red"), Car(2, "Lamborghini", "Azure"))
        every { repository.getCars() } returns expectedCars

        val actualCars = service.getCars()

        actualCars shouldBe expectedCars
    }

    @Test
    fun `getCar should return a car`() {
        val id = 1
        val expectedCar = Car(id, "Porsche", "Orange")
        every { repository.getCar(id) } returns Result.success(expectedCar)

        val result = service.getCar(1)

        result.getOrThrow() shouldBe expectedCar
    }

    @Test
    fun `getCar should return car not found`() {
        val id = 99
        every { repository.getCar(id) } returns Result.failure(NotFoundException())

        val result = service.getCar(99)

        result.isFailure shouldBe true
    }

    @Test
    fun `deleteCar should delete a car`() {
        val id = 2
        every { repository.deleteCar(id) } returns Result.success(true)

        val result = service.deleteCar(id)

        result.getOrThrow() shouldBe true
    }

    @Test
    fun `deleteCar should return car not found`() {
        val id = 99
        every { repository.deleteCar(id) } returns Result.success(false)

        val result = service.deleteCar(id)

        result.getOrThrow() shouldBe false
    }

    @Test
    fun `createCar should create a new car`() {
        val make = "Kia"
        val colour = "Indigo"
        val expectedCar = Car(7, make, colour)
        val command = CreateCarCommand(make, colour)
        every { repository.createCar(command) } returns Result.success(expectedCar)

        val result = service.createCar(command)

        result.isSuccess shouldBe true
        result.getOrThrow() shouldBe expectedCar
    }

    @Test
    fun `updateCar should update a car`() {
        val id = 8
        val make = "Rolls Royce"
        val colour = "Violet"
        val command = UpdateCarCommand(id, make, colour)
        val expectedCar = Car(id, make, colour)
        every { repository.updateCar(command) } returns Result.success(expectedCar)

        val result = service.updateCar(command)

        result.isSuccess shouldBe true
        result.getOrThrow() shouldBe expectedCar
    }

    @Test
    fun `updateCar should return car not found`() {
        val id = 99
        val make = "Cadillac"
        val colour = "Grey"
        val command = UpdateCarCommand(id, make, colour)
        every { repository.updateCar(command) } returns Result.failure(NotFoundException())

        val result = service.updateCar(command)

        result.isFailure shouldBe true
    }
}
