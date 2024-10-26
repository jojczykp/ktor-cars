package org.alterbit.assembler

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.alterbit.dto.UpdateCarCommand
import org.alterbit.rest.UpdateCarRequest

class UpdateCarAssemblerTest : ShouldSpec({
    
    val assembler = UpdateCarAssembler()

    should("create command from request propagating all fields") {
        val id = "26da4a6c-e7b3-4b0c-be1f-68e709753c2a"
        val request = UpdateCarRequest("make", "colour")

        val command = assembler.requestToCommand(id, request)

        command shouldBe UpdateCarCommand(id, "make", "colour")
    }

    should("create command from request propagating no fields") {
        val id = "40b2fee3-1569-4e46-bad9-b62e176051ff"
        val request = UpdateCarRequest()

        val command = assembler.requestToCommand(id, request)

        command shouldBe UpdateCarCommand(id)
    }

    should("create command from request propagating make") {
        val id = "a474d6b4-b447-4f6f-bf69-b88b6421e4b0"
        val request = UpdateCarRequest(make = "make")

        val command = assembler.requestToCommand(id, request)

        command shouldBe UpdateCarCommand(id = id, make = "make")
    }

    should("create command from request propagating colour") {
        val id = "be6a55a6-1fa9-4a73-84de-fbae3ae4ae47"
        val request = UpdateCarRequest(colour = "Pink")

        val command = assembler.requestToCommand(id, request)

        command shouldBe UpdateCarCommand(id = id, colour = "Pink")
    }
})
