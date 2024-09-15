package org.alterbit.repositories

import org.alterbit.model.Car

class CarsRepository {

    private val cars = mutableListOf(
        Car(1, "Audi"),
        Car(2, "BMW")
    )

    fun getCars(): List<Car> = cars

    fun getCar(id: Int): Car? = cars.find { it.id == id }
}
