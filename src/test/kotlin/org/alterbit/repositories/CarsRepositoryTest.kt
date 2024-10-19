package org.alterbit.repositories

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.alterbit.dto.CreateCarCommand
import org.alterbit.dto.UpdateCarCommand
import org.alterbit.model.Car
import org.alterbit.utils.PostgreSQLExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension


class CarsRepositoryTest {

    private val id1 = "67dd309e-8e9f-417e-9b1e-873d523d9597"
    private val id2 = "7dd22399-9334-416e-99c8-954a5537566e"
    private val id3 = "d6694921-543e-4ac3-aa3a-e7716cfbabda"
    private val id99 = "99999999-9999-9999-9999-999999999999"

    companion object {
        @RegisterExtension
        private val database = PostgreSQLExtension()
    }

    private val connection = database.connection

    private val repository = CarsRepository(connection)

    @Test
    fun `getCars should return cars`() {
        connection.createStatement().use {
            it.execute("INSERT INTO cars (id, make, colour) VALUES ('$id1', 'Audi', 'Red')")
            it.execute("INSERT INTO cars (id, make, colour) VALUES ('$id2', 'BMW', 'Blue')")
            it.execute("INSERT INTO cars (id, make, colour) VALUES ('$id3', 'Lexus', 'Pink')")
        }

        val result = repository.getCars()

        result shouldContainExactlyInAnyOrder listOf(
            Car(id1, "Audi", "Red"),
            Car(id2, "BMW", "Blue"),
            Car(id3, "Lexus", "Pink")
        )
    }

    @Test
    fun `getCar should return a car`() {
        connection.createStatement().apply {
            execute("INSERT INTO cars (id, make, colour) VALUES ('$id1', 'Audi', 'Red')")
            execute("INSERT INTO cars (id, make, colour) VALUES ('$id2', 'BMW', 'Blue')")
        }

        val result = repository.getCar(id1)

        result.isSuccess shouldBe true
        result.getOrThrow() shouldBe Car(id1, "Audi", "Red")
    }

    @Test
    fun `getCar should return car not found`() {
        val result = repository.getCar(id99)

        result.isFailure shouldBe true
    }

    @Test
    fun `deleteCar should delete a car`() {
        // Given
        connection.createStatement().apply {
            execute("INSERT INTO cars (id, make, colour) VALUES ('$id1', 'Audi', 'Red')")
            execute("INSERT INTO cars (id, make, colour) VALUES ('$id2', 'BMW', 'Blue')")
        }

        // When
        val result = repository.deleteCar(id2)

        // Then
        result.isSuccess shouldBe true
        result.getOrThrow() shouldBe true

        connection.createStatement().use {
            val next = it.executeQuery("SELECT * from cars WHERE id = '$id2'").next()
            next shouldBe false
        }
    }

    @Test
    fun `deleteCar should return car not found`() {
        val result = repository.deleteCar(id99)

        result.isFailure shouldBe true
    }

    @Test
    fun `createCar should create a new car`() {
        val command = CreateCarCommand("Kia", "Yellow")
        connection.createStatement().use {
            it.execute("INSERT INTO cars (id, make, colour) VALUES ('$id1', 'Audi', 'Red')")
            it.execute("INSERT INTO cars (id, make, colour) VALUES ('$id2', 'BMW', 'Blue')")
        }

        val result = repository.createCar(command)

        result.isSuccess shouldBe true
        val createdCar = result.getOrThrow()
        val expectedCar = Car(createdCar.id, "Kia", "Yellow")
        result.getOrThrow() shouldBe expectedCar
        repository.getCars() shouldContain expectedCar
    }

    @Test
    fun `updateCar should update all car properties`() {
        val command = UpdateCarCommand(id2, "Alfa Romeo", "Amber")
        connection.createStatement().apply {
            execute("INSERT INTO cars (id, make, colour) VALUES ('$id1', 'Audi', 'Red')")
            execute("INSERT INTO cars (id, make, colour) VALUES ('$id2', 'BMW', 'Blue')")
        }

        val result = repository.updateCar(command)

        result.isSuccess shouldBe true
        val expectedCar = Car(id2, "Alfa Romeo", "Amber")
        result.getOrThrow() shouldBe expectedCar
        repository.getCars() shouldContain expectedCar
    }

    @Test
    fun `updateCar should update make`() {
        val command = UpdateCarCommand(id = id2, make = "Cupra")
        connection.createStatement().apply {
            execute("INSERT INTO cars (id, make, colour) VALUES ('$id1', 'Audi', 'Red')")
            execute("INSERT INTO cars (id, make, colour) VALUES ('$id2', 'BMW', 'Blue')")
        }

        val result = repository.updateCar(command)

        result.isSuccess shouldBe true
        val expectedCar = Car(id2, "Cupra", "Blue")
        result.getOrThrow() shouldBe expectedCar
        repository.getCars() shouldContain expectedCar
    }

    @Test
    fun `updateCar should update colour`() {
        val command = UpdateCarCommand(id = id2, colour = "White")
        connection.createStatement().apply {
            execute("INSERT INTO cars (id, make, colour) VALUES ('$id1', 'Audi', 'Red')")
            execute("INSERT INTO cars (id, make, colour) VALUES ('$id2', 'BMW', 'Blue')")
        }

        val result = repository.updateCar(command)

        result.isSuccess shouldBe true
        val expectedCar = Car(id2, "BMW", "White")
        result.getOrThrow() shouldBe expectedCar
        repository.getCars() shouldContain expectedCar
    }

    @Test
    fun `updateCar should return car not found`() {
        val command = UpdateCarCommand(id = id99)

        val result = repository.updateCar(command)

        result.isFailure shouldBe true
    }
}
