package org.alterbit.assembler

import io.kotest.matchers.shouldBe
import org.alterbit.dto.UpdateCarCommand
import org.alterbit.rest.UpdateCarRequest
import org.junit.jupiter.api.Test

class UpdateCarAssemblerTest {
    
    private val assembler = UpdateCarAssembler()

    @Test
    fun `should create command from request propagating all fields`() {
        val id = 7
        val request = UpdateCarRequest("make", "colour")

        val command = assembler.requestToCommand(id, request)

        command shouldBe UpdateCarCommand(id, "make", "colour")
    }

    @Test
    fun `should create command from request propagating no fields`() {
        val id = 7
        val request = UpdateCarRequest()

        val command = assembler.requestToCommand(id, request)

        command shouldBe UpdateCarCommand(id)
    }

    @Test
    fun `should create command from request propagating make`() {
        val id = 7
        val request = UpdateCarRequest(make = "make")

        val command = assembler.requestToCommand(id, request)

        command shouldBe UpdateCarCommand(id = id, make = "make")
    }

    @Test
    fun `should create command from request propagating colour`() {
        val id = 7
        val request = UpdateCarRequest(colour = "Pink")

        val command = assembler.requestToCommand(id, request)

        command shouldBe UpdateCarCommand(id = id, colour = "Pink")
    }
}
