package org.alterbit.database

import org.alterbit.model.Car
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class CarsRowMapper : RowMapper<Car> {
    override fun map(rs: ResultSet, ctx: StatementContext?): Car {
        return Car(
            rs.getString("id"),
            rs.getString("make"),
            rs.getString("colour"))
    }
}
