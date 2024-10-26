package org.alterbit.database.cars

import org.alterbit.commands.CreateCarCommand
import org.alterbit.commands.UpdateCarCommand
import org.alterbit.model.Car
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterRowMapper(CarsRowMapper::class)
interface CarsDao {

    @SqlQuery("SELECT * FROM cars")
    fun getCars(): Set<Car>

    @SqlQuery("SELECT * FROM cars WHERE id = :id")
    fun getCar(id: String): Car?

    @SqlUpdate("""
        INSERT INTO cars
            (id , make, colour)
        VALUES
            (:id, :command.make, :command.colour)
        """)
    fun createCar(id: String, command: CreateCarCommand): Boolean

    @SqlUpdate("DELETE FROM cars WHERE id = :id")
    fun deleteCar(id: String): Boolean

    @SqlUpdate("""
        UPDATE cars
        SET
            make   = (CASE WHEN :command.make   IS NULL THEN make   ELSE :command.make   END),
            colour = (CASE WHEN :command.colour IS NULL THEN colour ELSE :command.colour END)
        WHERE
            id = :command.id
        """)
    fun updateCar(command: UpdateCarCommand): Boolean
}
