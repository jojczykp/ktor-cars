package org.alterbit.repositories

import io.ktor.server.plugins.*
import org.alterbit.dto.CreateCarCommand
import org.alterbit.dto.UpdateCarCommand
import org.alterbit.model.Car
import java.sql.Connection
import java.sql.ResultSet
import java.util.*

class CarsRepository(private val connection: Connection) {

    fun getCars(): Set<Car> = connection
        .createStatement()
        .executeQuery("SELECT id, make, colour FROM cars")
        .asSequence()
        .map { it.toCar() }
        .toSet()

    fun getCar(id: String): Result<Car> = connection
        .createStatement()
        .executeQuery("SELECT id, make, colour FROM cars WHERE id = '$id'")
        .let { rs ->
            if (rs.next()) {
                Result.success(rs.toCar())
            } else {
                Result.failure(NotFoundException("Car $id not found"))
            }
        }

    fun createCar(command: CreateCarCommand): Result<Car> {
        val id = UUID.randomUUID().toString()

        connection
            .createStatement()
            .executeUpdate("INSERT INTO cars (id, make, colour) VALUES ('$id', '${command.make}', '${command.colour}')")

        return getCar(id)
    }

    fun deleteCar(id: String): Result<Boolean> = connection
        .createStatement()
        .executeUpdate("DELETE FROM cars WHERE id = '$id'")
        .let {
            if (it > 0) {
                Result.success(true)
            } else {
                Result.failure(NotFoundException("Car $id not found"))
            }
        }

    fun updateCar(command: UpdateCarCommand): Result<Car> {
        val updates = command.updates();

        if (updates.isNotBlank()) {
            connection
                .createStatement()
                .executeUpdate("UPDATE cars SET $updates WHERE id = '${command.id}'")
        }

        return getCar(command.id)
    }

    private fun UpdateCarCommand.updates(): String {
        val result = mutableListOf<String>()

        if (make != null) {
            result.add("make = '$make'")
        }

        if (colour != null) {
            result.add("colour = '$colour'")
        }

        return result.joinToString()
    }

    private fun ResultSet.asSequence(): Sequence<ResultSet> = sequence {
        while (this@asSequence.next()) {
            yield(this@asSequence)
        }
    }

    private fun ResultSet.toCar() = Car(
        getString("id"),
        getString("make"),
        getString("colour")
    )
}
