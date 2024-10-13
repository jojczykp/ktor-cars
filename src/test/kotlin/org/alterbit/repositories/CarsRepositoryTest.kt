package org.alterbit.repositories

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.alterbit.dto.CreateCarCommand
import org.alterbit.dto.UpdateCarCommand
import org.alterbit.model.Car
import org.junit.jupiter.api.Test

class CarsRepositoryTest {

    @Test
    fun `getCars should return cars`() {
        val repository = CarsRepository()

        val result = repository.getCars()

        result shouldContainExactly listOf(
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

        result.isSuccess shouldBe true
        result.getOrThrow() shouldBe Car(1, "Audi", "Red")
    }

    @Test
    fun `getCar should return car not found`() {
        val repository = CarsRepository()

        val result = repository.getCar(99)

        result.isFailure shouldBe true
    }

    @Test
    fun `deleteCar should delete a car`() {
        val repository = CarsRepository()

        val result = repository.deleteCar(2)

        result.isSuccess shouldBe true
        result.getOrThrow() shouldBe true
    }

    @Test
    fun `deleteCar should return car not found`() {
        val repository = CarsRepository()

        val result = repository.getCar(99)

        result.isFailure shouldBe true
    }

    @Test
    fun `createCar should create a new car`() {
        val repository = CarsRepository()
        val command = CreateCarCommand("Kia", "Yellow")

        val result = repository.createCar(command)

        val expectedCar = Car(6, "Kia", "Yellow")
        result.isSuccess shouldBe true
        result.getOrThrow() shouldBe expectedCar
        repository.getCars() shouldContain expectedCar
    }

    @Test
    fun `updateCar should update all car properties`() {
        val repository = CarsRepository()
        val command = UpdateCarCommand(3, "Alfa Romeo", "Amber")

        val result = repository.updateCar(command)

        val expectedCar = Car(3, "Alfa Romeo", "Amber")
        result.isSuccess shouldBe true
        result.getOrThrow() shouldBe expectedCar
        repository.getCars() shouldContain expectedCar
    }

    @Test
    fun `updateCar should update make`() {
        val repository = CarsRepository()
        val command = UpdateCarCommand(id = 4, make = "Cupra")

        val result = repository.updateCar(command)

        val expectedCar = Car(4, "Cupra", "Brown")
        result.isSuccess shouldBe true
        result.getOrThrow() shouldBe expectedCar
        repository.getCars() shouldContain expectedCar
    }

    @Test
    fun `updateCar should update colour`() {
        val repository = CarsRepository()
        val command = UpdateCarCommand(id = 5, colour = "White")

        val result = repository.updateCar(command)

        val expectedCar = Car(5, "Ford", "White")
        result.isSuccess shouldBe true
        result.getOrThrow() shouldBe expectedCar
        repository.getCars() shouldContain expectedCar
    }

    @Test
    fun `updateCar should return car not found`() {
        val repository = CarsRepository()
        val command = UpdateCarCommand(id = 99)

        val result = repository.updateCar(command)

        result.isFailure shouldBe true
    }
}
