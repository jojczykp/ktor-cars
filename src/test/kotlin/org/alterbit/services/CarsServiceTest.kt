package org.alterbit.services

import io.ktor.server.plugins.*
import org.alterbit.dto.CreateCarCommand
import org.alterbit.dto.UpdateCarCommand
import org.alterbit.model.Car
import org.alterbit.repositories.CarsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class CarsServiceTest {

    @Test
    fun `getCars should return cars`() {
        val expectedCars = setOf(Car(1, "Porsche", "Red"), Car(2, "Lamborghini", "Azure"))
        val repository = mock<CarsRepository> { on { getCars() } doReturn expectedCars }
        val service = CarsService(repository)

        val actualCars = service.getCars()

        assertThat(actualCars).isEqualTo(expectedCars)
    }

    @Test
    fun `getCar should return a car`() {
        val id = 1
        val expectedCar = Car(id, "Porsche", "Orange")
        val repository = mock<CarsRepository> { on { getCar(id) } doReturn Result.success(expectedCar) }
        val service = CarsService(repository)

        val result = service.getCar(1)

        assertThat(result.getOrThrow()).isEqualTo(expectedCar)
    }

    @Test
    fun `getCar should return car not found`() {
        val id = 99
        val repository = mock<CarsRepository> { on { getCar(id) } doReturn Result.failure(NotFoundException()) }
        val service = CarsService(repository)

        val result = service.getCar(99)

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `deleteCar should delete a car`() {
        val id = 2
        val repository = mock<CarsRepository> { on { deleteCar(id) } doReturn Result.success(true) }
        val service = CarsService(repository)

        val result = service.deleteCar(id)

        assertThat(result.getOrThrow()).isTrue()
    }

    @Test
    fun `deleteCar should return car not found`() {
        val id = 99
        val repository = mock<CarsRepository> { on { deleteCar(id) } doReturn Result.success(false) }
        val service = CarsService(repository)

        val result = service.deleteCar(id)

        assertThat(result.getOrThrow()).isFalse()
    }

    @Test
    fun `createCar should create a new car`() {
        val make = "Kia"
        val colour = "Indigo"
        val expectedCar = Car(7, make, colour)
        val command = CreateCarCommand(make, colour)
        val repository = mock<CarsRepository> { on { createCar(command) } doReturn Result.success(expectedCar) }
        val service = CarsService(repository)

        val result = service.createCar(command)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isEqualTo(expectedCar)
    }

    @Test
    fun `updateCar should update a car`() {
        val id = 8
        val make = "Rolls Royce"
        val colour = "Violet"
        val command = UpdateCarCommand(id, make, colour)
        val expectedCar = Car(id, make, colour)
        val repository = mock<CarsRepository> { on { updateCar(command) } doReturn Result.success(expectedCar) }
        val service = CarsService(repository)

        val result = service.updateCar(command)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isEqualTo(expectedCar)
    }

    @Test
    fun `updateCar should return car not found`() {
        val id = 99
        val make = "Cadillac"
        val colour = "Grey"
        val command = UpdateCarCommand(id, make, colour)
        val repository = mock<CarsRepository> { on { updateCar(command) } doReturn Result.failure(NotFoundException()) }
        val service = CarsService(repository)

        val result = service.updateCar(command)

        assertThat(result.isFailure).isTrue()
    }
}
