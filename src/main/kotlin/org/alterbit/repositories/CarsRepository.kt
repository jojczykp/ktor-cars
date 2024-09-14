package org.alterbit.repositories

import org.alterbit.model.Car

class CarsRepository {
    private val cars = mutableListOf(
        Car(1, "Audi"),
        Car(2, "BMW")
    )

    fun getCars(): List<Car> = cars
}
