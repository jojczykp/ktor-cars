package org.alterbit.ktorcars.database.cars

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.string.shouldMatch

class CarsIdGeneratorTest : ShouldSpec({

    val carsIdGenerator = CarsIdGenerator()

    should("generate car id") {
        val result = carsIdGenerator.newId()

        result shouldMatch "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$"
    }
})
