package org.alterbit.assembler

import org.alterbit.commands.CreateCarCommand
import org.alterbit.rest.CreateCarRequest

class CreateCarAssembler {

    fun requestToCommand(requestBody: CreateCarRequest): CreateCarCommand =
        CreateCarCommand(requestBody.make, requestBody.colour)
}