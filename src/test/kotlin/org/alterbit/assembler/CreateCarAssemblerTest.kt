package org.alterbit.assembler

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.alterbit.dto.CreateCarCommand
import org.alterbit.rest.CreateCarRequest

class CreateCarAssemblerTest : ShouldSpec({

    val assembler = CreateCarAssembler()

    should("create command from request") {
        val request = CreateCarRequest("make", "colour")

        val command = assembler.requestToCommand(request)

        command shouldBe CreateCarCommand("make", "colour")
    }
})
