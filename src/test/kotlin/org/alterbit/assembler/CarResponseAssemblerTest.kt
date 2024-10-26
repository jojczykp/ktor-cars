package org.alterbit.assembler

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.alterbit.model.Car
import org.alterbit.rest.CarResponse

class CarResponseAssemblerTest : ShouldSpec({

    val assembler = CarResponseAssembler()

    should("create response from car") {
        val id = "17369f5e-1669-43cf-802d-1d55aa570527"
        val car = Car(id, "make", "colour")

        val response = assembler.carToResponse(car)

        response shouldBe CarResponse(id, "make", "colour")
    }
})
