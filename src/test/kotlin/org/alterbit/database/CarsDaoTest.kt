package org.alterbit.database

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.alterbit.dto.CreateCarCommand
import org.alterbit.dto.UpdateCarCommand
import org.alterbit.model.Car
import org.alterbit.utils.PostgreSQLExtension
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension


class CarsDaoTest {

    companion object {
        @RegisterExtension
        private val database = PostgreSQLExtension()
    }

    private val dao = Jdbi.create(database.dataSource).installPlugins().onDemand(CarsDao::class.java)

    @Test
    fun `getCars should return cars`() {
        // Given
        val id1 = "0684c5f5-cc63-426e-a86d-da3f7e6521ad"
        val id2 = "ab343f4d-101f-4c75-985f-601f648336c9"
        val id3 = "07471545-ce58-4ea7-8160-79f89b15c1d4"

        database.dataSource.connection.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute("INSERT INTO cars (id, make, colour) VALUES ('$id1', 'Audi', 'Red')")
                stmt.execute("INSERT INTO cars (id, make, colour) VALUES ('$id2', 'BMW', 'Blue')")
                stmt.execute("INSERT INTO cars (id, make, colour) VALUES ('$id3', 'Lexus', 'Pink')")
            }
        }

        // When
        val result = dao.getCars()

        // Then
        result shouldContain Car(id1, "Audi", "Red")
        result shouldContain Car(id2, "BMW", "Blue")
        result shouldContain Car(id3, "Lexus", "Pink")
    }

    @Test
    fun `getCar should return a car`() {
        // Given
        val id1 = "c56c2b2f-1c64-4149-80c1-a630c796bef6"
        val id2 = "71dbc2b4-e332-4fe2-bd28-019905a21efa"

        database.dataSource.connection.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute("INSERT INTO cars (id, make, colour) VALUES ('$id1', 'Audi', 'Red')")
                stmt.execute("INSERT INTO cars (id, make, colour) VALUES ('$id2', 'BMW', 'Blue')")
            }
        }

        // When
        val result = dao.getCar(id1)

        // Then
        result shouldBe Car(id1, "Audi", "Red")
    }

    @Test
    fun `getCar should return car not found`() {
        val id99 = "99999999-9999-9999-9999-999999999999"

        val result = dao.getCar(id99)

        result shouldBe null
    }

    @Test
    fun `deleteCar should delete a car`() {
        database.dataSource.connection.use { conn ->
            // Given
            val id1 = "51d2ba56-a873-4876-b54e-045fff4fbcbd"
            val id2 = "ec4df559-0f05-40e4-ac7e-c543348379fa"

            conn.createStatement().use { stmt ->
                stmt.execute("INSERT INTO cars (id, make, colour) VALUES ('$id1', 'Audi', 'Red')")
                stmt.execute("INSERT INTO cars (id, make, colour) VALUES ('$id2', 'BMW', 'Blue')")
            }

            // When
            val result = dao.deleteCar(id2)

            // Then
            result shouldBe true

            conn.createStatement().use { stmt ->
                val next = stmt.executeQuery("SELECT * from cars WHERE id = '$id2'").next()
                next shouldBe false
            }
        }
    }

    @Test
    fun `deleteCar should return car not found`() {
        val id99 = "99999999-9999-9999-9999-999999999999"

        val result = dao.deleteCar(id99)

        result shouldBe false
    }

    @Test
    fun `createCar should create a new car`() {
        // Given
        val id1 = "bf108c80-22e2-4ceb-baaf-d1d5df1fd5f3"
        val id2 = "d43bbade-dab8-406b-bcb3-b4086d767764"
        val id3 = "934a2d43-d80b-4305-a338-367bfa9ba664"

        database.dataSource.connection.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute("INSERT INTO cars (id, make, colour) VALUES ('$id1', 'Audi', 'Red')")
                stmt.execute("INSERT INTO cars (id, make, colour) VALUES ('$id2', 'BMW', 'Blue')")
            }
        }

        val command = CreateCarCommand("Kia", "Yellow")

        // When
        val result = dao.createCar(id3, command)

        // Then
        result shouldBe true
        val createdCar = dao.getCar(id3)
        createdCar shouldBe Car(id3, "Kia", "Yellow")
    }

    @Test
    fun `updateCar should update all car properties`() {
        // Given
        val id1 = "2c22b81d-baf5-4dc8-b699-cff49e8cec6e"
        val id2 = "99031e63-490f-45fc-b18e-d3df01b8fe94"

        database.dataSource.connection.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute("INSERT INTO cars (id, make, colour) VALUES ('$id1', 'Audi', 'Red')")
                stmt.execute("INSERT INTO cars (id, make, colour) VALUES ('$id2', 'BMW', 'Blue')")
            }
        }

        val command = UpdateCarCommand(id2, "Alfa Romeo", "Amber")

        // When
        val result = dao.updateCar(command)

        // Then
        result shouldBe true
        val updatedCar = dao.getCar(id2)
        updatedCar shouldBe Car(id2, "Alfa Romeo", "Amber")
    }

    @Test
    fun `updateCar should update make`() {
        // Given
        val id1 = "2e282b36-b05e-435c-9c89-d58f1426a158"
        val id2 = "240ac987-3353-43f9-ba59-046aa4394755"

        database.dataSource.connection.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute("INSERT INTO cars (id, make, colour) VALUES ('$id1', 'Audi', 'Red')")
                stmt.execute("INSERT INTO cars (id, make, colour) VALUES ('$id2', 'BMW', 'Blue')")
            }
        }

        val command = UpdateCarCommand(id = id2, make = "Cupra")

        // When
        val result = dao.updateCar(command)

        // Then
        result shouldBe true
        val updatedCar = dao.getCar(id2)
        updatedCar shouldBe Car(id2, "Cupra", "Blue")
    }

    @Test
    fun `updateCar should update colour`() {
        // Given
        val id1 = "c1a19087-d50f-416c-ab11-fe5375cec4b6"
        val id2 = "e625081a-0ee4-4720-9548-317170fd1265"

        database.dataSource.connection.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.execute("INSERT INTO cars (id, make, colour) VALUES ('$id1', 'Audi', 'Red')")
                stmt.execute("INSERT INTO cars (id, make, colour) VALUES ('$id2', 'BMW', 'Blue')")
            }
        }

        val command = UpdateCarCommand(id = id2, colour = "White")

        // When
        val result = dao.updateCar(command)

        // Then
        result shouldBe true
        val updatedCar = dao.getCar(id2)
        updatedCar shouldBe Car(id2, "BMW", "White")

    }

    @Test
    fun `updateCar should return car not found`() {
        val id99 = "99999999-9999-9999-9999-999999999999"
        val command = UpdateCarCommand(id = id99)

        val result = dao.updateCar(command)

        result shouldBe false
    }
}
