package org.alterbit.repositories

import io.ktor.server.plugins.*
import org.alterbit.model.Car

class CarsRepository {

    private val cars = mutableListOf(
        Car(1, "Audi"),
        Car(2, "BMW")
    )

    fun getCars(): List<Car> = cars

    fun getCar(id: Int): Result<Car> =
        cars.find { it.id == id }
            .let {
                if (it == null) Result.failure(NotFoundException("Car $id not found"))
                else Result.success(it)
            }

    fun createCar(make: String): Result<Car> =
        cars.maxOfOrNull { existingCar -> existingCar.id }
            .let { lastId -> (lastId ?: 0) + 1 }
            .let { newId -> Car(newId, make) }
            .let { newCar -> cars.add(newCar); newCar }
            .let { newCar -> Result.success(newCar) }

    fun deleteCar(id: Int): Result<Boolean> =
        cars.removeIf { it.id == id }
            .let { Result.success(it) }
}
