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
}
