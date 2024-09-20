package org.alterbit.dto

import org.alterbit.rest.UpdateCarRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UpdateCarCommandTest {

    @Test
    fun `should create command from request propagating all fields`() {
        val id = 7
        val request = UpdateCarRequest("make", "colour")

        val command = UpdateCarCommand.fromRequest(id, request)

        assertThat(command).isEqualTo(UpdateCarCommand(id, "make", "colour"))
    }

    @Test
    fun `should create command from request propagating no fields`() {
        val id = 7
        val request = UpdateCarRequest()

        val command = UpdateCarCommand.fromRequest(id, request)

        assertThat(command).isEqualTo(UpdateCarCommand(id))
    }

    @Test
    fun `should create command from request propagating make`() {
        val id = 7
        val request = UpdateCarRequest(make = "make")

        val command = UpdateCarCommand.fromRequest(id, request)

        assertThat(command).isEqualTo(UpdateCarCommand(id = id, make = "make"))
    }

    @Test
    fun `should create command from request propagating colour`() {
        val id = 7
        val request = UpdateCarRequest(colour = "Pink")

        val command = UpdateCarCommand.fromRequest(id, request)

        assertThat(command).isEqualTo(UpdateCarCommand(id = id, colour = "Pink"))
    }
}
