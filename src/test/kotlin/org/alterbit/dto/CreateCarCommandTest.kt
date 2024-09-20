package org.alterbit.dto

import org.alterbit.rest.CreateCarRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreateCarCommandTest {

    @Test
    fun `should create command from request propagating all fields`() {
        val request = CreateCarRequest("make", "colour")

        val command = CreateCarCommand.fromRequest(request)

        assertThat(command).isEqualTo(CreateCarCommand("make", "colour"))
    }
}
