package org.alterbit.services

import io.ktor.server.plugins.*
import org.alterbit.model.Car
import org.alterbit.repositories.CarsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class CarsServiceTest {

    @Test
    fun `getCars should return cars`() {
        val cars = setOf(Car(1, "Porsche", "Red"), Car(2, "Lamborghini", "Azure"))
        val repository = mock<CarsRepository> { on { getCars() } doReturn cars }
        val service = CarsService(repository)

        val result = service.getCars()

        assertThat(result).isEqualTo(cars)
    }

    @Test
    fun `getCar should return a car`() {
        val id = 1
        val car = Car(id, "Porsche", "Orange")
        val repository = mock<CarsRepository> { on { getCar(id) } doReturn Result.success(car) }
        val service = CarsService(repository)

        val result = service.getCar(1)

        assertThat(result.getOrThrow()).isEqualTo(car)
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
        val car = Car(7, make, colour)
        val repository = mock<CarsRepository> { on { createCar(make, colour) } doReturn Result.success(car) }
        val service = CarsService(repository)

        val result = service.createCar(make, colour)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isEqualTo(car)
    }

    @Test
    fun `updateCar should update a car`() {
        val id = 8
        val make = "Rolls Royce"
        val colour = "Violet"
        val car = Car(id, make, colour)
        val repository = mock<CarsRepository> { on { updateCar(id, make, colour) } doReturn Result.success(car) }
        val service = CarsService(repository)

        val result = service.updateCar(id, make, colour)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isEqualTo(car)
    }

    @Test
    fun `updateCar should return car not found`() {
        val id = 99
        val make = "Cadillac"
        val colour = "Grey"
        val repository = mock<CarsRepository> { on { updateCar(id, make, colour) } doReturn Result.failure(NotFoundException()) }
        val service = CarsService(repository)

        val result = service.updateCar(id, make, colour)

        assertThat(result.isFailure).isTrue()
    }
}
