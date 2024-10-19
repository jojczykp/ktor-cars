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
        val expectedCars = setOf(
            Car("c37da7b1-8dbe-4b8e-8580-1340eb9e6f27", "Porsche", "Red"),
            Car("946efb67-35de-437c-97b7-bc20e8cc69fe", "Lamborghini", "Azure"))
        every { repository.getCars() } returns expectedCars

        val actualCars = service.getCars()

        actualCars shouldBe expectedCars
    }

    @Test
    fun `getCar should return a car`() {
        val id = "792533c3-3451-4b31-ae2d-be8babdd67c5"
        val expectedCar = Car(id, "Porsche", "Orange")
        every { repository.getCar(id) } returns Result.success(expectedCar)

        val result = service.getCar(id)

        result.getOrThrow() shouldBe expectedCar
    }

    @Test
    fun `getCar should return car not found`() {
        val id = "f2c2c263-e6be-49ad-a703-02237280d05a"
        every { repository.getCar(id) } returns Result.failure(NotFoundException())

        val result = service.getCar(id)

        result.isFailure shouldBe true
    }

    @Test
    fun `deleteCar should delete a car`() {
        val id = "b8dac893-38c5-421d-a66b-51aec22b5770"
        every { repository.deleteCar(id) } returns Result.success(true)

        val result = service.deleteCar(id)

        result.getOrThrow() shouldBe true
    }

    @Test
    fun `deleteCar should return car not found`() {
        val id = "dd745d2b-d9d2-48cf-be8a-7e091abce097"
        every { repository.deleteCar(id) } returns Result.success(false)

        val result = service.deleteCar(id)

        result.getOrThrow() shouldBe false
    }

    @Test
    fun `createCar should create a new car`() {
        val id = "f2fd207f-6578-43e5-bc7c-5368c6ae43a5"
        val make = "Kia"
        val colour = "Indigo"
        val expectedCar = Car(id, make, colour)
        val command = CreateCarCommand(make, colour)
        every { repository.createCar(command) } returns Result.success(expectedCar)

        val result = service.createCar(command)

        result.isSuccess shouldBe true
        result.getOrThrow() shouldBe expectedCar
    }

    @Test
    fun `updateCar should update a car`() {
        val id = "9ab8d756-5512-4dc0-8cf0-4528bcf1de24"
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
        val id = "240ee34f-5bdb-4a4b-b5b7-fc54bbcf0e53"
        val make = "Cadillac"
        val colour = "Grey"
        val command = UpdateCarCommand(id, make, colour)
        every { repository.updateCar(command) } returns Result.failure(NotFoundException())

        val result = service.updateCar(command)

        result.isFailure shouldBe true
    }
}
