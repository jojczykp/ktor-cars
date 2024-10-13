package org.alterbit.assembler

import io.kotest.matchers.shouldBe
import org.alterbit.model.Car
import org.alterbit.rest.CarResponse
import org.junit.jupiter.api.Test

class CarResponseAssemblerTest {

    private val assembler = CarResponseAssembler()

    @Test
    fun `should create response from car`() {
        val car = Car(5, "make", "colour")

        val response = assembler.carToResponse(car)

        response shouldBe CarResponse(5, "make", "colour")
    }
}
