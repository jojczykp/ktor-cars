package org.alterbit.assembler

import io.kotest.matchers.shouldBe
import org.alterbit.dto.CreateCarCommand
import org.alterbit.rest.CreateCarRequest
import org.junit.jupiter.api.Test

class CreateCarAssemblerTest {

    private val assembler = CreateCarAssembler()

    @Test
    fun `should create command from request`() {
        val request = CreateCarRequest("make", "colour")

        val command = assembler.requestToCommand(request)

        command shouldBe CreateCarCommand("make", "colour")
    }
}
