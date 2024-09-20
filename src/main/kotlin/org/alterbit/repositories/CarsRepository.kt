package org.alterbit.repositories

import io.ktor.server.plugins.*
import org.alterbit.dto.CreateCarCommand
import org.alterbit.dto.UpdateCarCommand
import org.alterbit.model.Car

class CarsRepository {

    private val cars = listOf(
        Car(1, "Audi", "Red"),
        Car(2, "BMW", "Blue"),
        Car(3, "Lexus", "Pink"),
        Car(4, "Renault", "Brown"),
        Car(5, "Ford", "Green")
    ).associateBy { it.id }.toMutableMap()

    fun getCars(): Set<Car> = cars.values.toSet()

    fun getCar(id: Int): Result<Car> =
        cars[id]
            .let {
                if (it == null) Result.failure(NotFoundException("Car $id not found"))
                else Result.success(it)
            }

    fun createCar(command: CreateCarCommand): Result<Car> =
        cars.keys
            .maxOrNull()
            .let { lastId -> (lastId ?: 0) + 1 }
            .let { newId -> Car(newId, command.make, command.colour) }
            .let { newCar -> cars[newCar.id] = newCar; newCar }
            .let { newCar -> Result.success(newCar) }

    fun deleteCar(id: Int): Result<Boolean> =
        cars.remove(id)
            .let { Result.success(it != null) }

    fun updateCar(command: UpdateCarCommand): Result<Car> =
        cars[command.id]
            .let { originalCar ->
                if (originalCar == null) Result.failure(NotFoundException("Car ${command.id} not found"))
                else {
                    val updatedCar = originalCar.copy(
                        make = command.make ?: originalCar.make,
                        colour = command.colour ?: originalCar.colour
                    )
                    cars[updatedCar.id] = updatedCar
                    Result.success(updatedCar)
                }
            }
}
