package org.alterbit.repositories

import io.ktor.server.plugins.*
import org.alterbit.model.Car

class CarsRepository {

    private val cars = mutableMapOf(
        1 to Car(1, "Audi"),
        2 to Car(2, "BMW"),
        3 to Car(3, "Lexus")
    )

    fun getCars(): Set<Car> = cars.values.toSet()

    fun getCar(id: Int): Result<Car> =
        cars[id]
            .let {
                if (it == null) Result.failure(NotFoundException("Car $id not found"))
                else Result.success(it)
            }

    fun createCar(make: String): Result<Car> =
        cars.keys
            .maxOrNull()
            .let { lastId -> (lastId ?: 0) + 1 }
            .let { newId -> Car(newId, make) }
            .let { newCar -> cars[newCar.id] = newCar; newCar }
            .let { newCar -> Result.success(newCar) }

    fun deleteCar(id: Int): Result<Boolean> =
        cars.remove(id)
            .let { Result.success(it != null) }

    fun updateCar(id: Int, make: String): Result<Car> =
        cars[id]
            .let { originalCar ->
                if (originalCar == null) Result.failure(NotFoundException("Car $id not found"))
                else {
                    val updatedCar = originalCar.copy(make = make)
                    cars[updatedCar.id] = updatedCar
                    Result.success(updatedCar)
                }
            }
}
