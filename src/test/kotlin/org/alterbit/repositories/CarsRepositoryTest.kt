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
            Car(1, "Audi"),
            Car(2, "BMW")
        )
    }

    @Test
    fun `getCar should return a car`() {
        val repository = CarsRepository()

        val result = repository.getCar(1)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isEqualTo(Car(1, "Audi"))
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

        val result = repository.createCar("Kia")

        val expectedCar = Car(3, "Kia")
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isEqualTo(expectedCar)
        assertThat(repository.getCars()).contains(expectedCar)
    }
}
