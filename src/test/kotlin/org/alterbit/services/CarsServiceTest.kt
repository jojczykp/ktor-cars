package org.alterbit.services

import org.alterbit.model.Car
import org.alterbit.repositories.CarsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class CarsServiceTest {

    @Test
    fun `getCars should return cars`() {
        val cars = mutableListOf(Car(1, "Porshe"), Car(2, "Lamborgini"))
        val repository = mock<CarsRepository> { on { getCars() } doReturn cars }
        val service = CarsService(repository)

        val result = service.getCars()

        assertThat(result).isEqualTo(cars)
    }

    @Test
    fun `getCar should return car`() {
        val id = 1
        val car = Car(id, "Porshe")
        val repository = mock<CarsRepository> { on { getCar(id) } doReturn car }
        val service = CarsService(repository)

        val result = service.getCar(1)

        assertThat(result).isEqualTo(car)
    }

    @Test
    fun `getCar should return car not found`() {
        val id = 99
        val repository = mock<CarsRepository> { on { getCar(id) } doReturn null }
        val service = CarsService(repository)

        val result = service.getCar(99)

        assertThat(result).isNull()
    }
}
