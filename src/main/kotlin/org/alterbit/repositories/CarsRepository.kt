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
}
