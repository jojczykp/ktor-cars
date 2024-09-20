package org.alterbit.assembler

import org.alterbit.model.Car
import org.alterbit.rest.CarResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CarResponseAssemblerTest {

    private val assembler = CarResponseAssembler()

    @Test
    fun `should create response from car`() {
        val car = Car(5, "make", "colour")

        val response = assembler.carToResponse(car)

        assertThat(response).isEqualTo(CarResponse(5, "make", "colour"))
    }
}
