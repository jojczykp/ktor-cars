package org.alterbit.repositories

import org.alterbit.model.Car
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CarsRepositoryTest {

    @Test
    fun `getCars should return cars`() {
        val repository = CarsRepository()

        val result = repository.getCars()

        assertThat(result).containsExactly(
            Car(1, "Audi", "Red"),
            Car(2, "BMW", "Blue"),
            Car(3, "Lexus", "Pink"),
            Car(4, "Renault", "Brown"),
            Car(5, "Ford", "Green")
        )
    }

    @Test
    fun `getCar should return a car`() {
        val repository = CarsRepository()

        val result = repository.getCar(1)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isEqualTo(Car(1, "Audi", "Red"))
    }

    @Test
    fun `getCar should return car not found`() {
        val repository = CarsRepository()

        val result = repository.getCar(99)

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `deleteCar should delete a car`() {
        val repository = CarsRepository()

        val result = repository.deleteCar(2)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isTrue()
    }

    @Test
    fun `deleteCar should return car not found`() {
        val repository = CarsRepository()

        val result = repository.getCar(99)

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `createCar should create a new car`() {
        val repository = CarsRepository()

        val result = repository.createCar("Kia", "Yellow")

        val expectedCar = Car(6, "Kia", "Yellow")
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isEqualTo(expectedCar)
        assertThat(repository.getCars()).contains(expectedCar)
    }

    @Test
    fun `updateCar should update all car properties`() {
        val repository = CarsRepository()

        val result = repository.updateCar(3, "Alfa Romeo", "Amber")

        val expectedCar = Car(3, "Alfa Romeo", "Amber")
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isEqualTo(expectedCar)
        assertThat(repository.getCars()).contains(expectedCar)
    }

    @Test
    fun `updateCar should update make`() {
        val repository = CarsRepository()

        val result = repository.updateCar(id = 4, make = "Cupra")

        val expectedCar = Car(4, "Cupra", "Brown")
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isEqualTo(expectedCar)
        assertThat(repository.getCars()).contains(expectedCar)
    }

    @Test
    fun `updateCar should update colour`() {
        val repository = CarsRepository()

        val result = repository.updateCar(5, colour = "White")

        val expectedCar = Car(5, "Ford", "White")
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isEqualTo(expectedCar)
        assertThat(repository.getCars()).contains(expectedCar)
    }

    @Test
    fun `updateCar should return car not found`() {
        val repository = CarsRepository()

        val result = repository.updateCar(99, "Mitsubishi", "Silver")

        assertThat(result.isFailure).isTrue()
    }
}
