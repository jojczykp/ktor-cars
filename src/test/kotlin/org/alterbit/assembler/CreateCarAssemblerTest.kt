package org.alterbit.assembler

import org.alterbit.dto.CreateCarCommand
import org.alterbit.rest.CreateCarRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreateCarAssemblerTest {

    private val assembler = CreateCarAssembler()

    @Test
    fun `should create command from request`() {
        val request = CreateCarRequest("make", "colour")

        val command = assembler.requestToCommand(request)

        assertThat(command).isEqualTo(CreateCarCommand("make", "colour"))
    }
}
