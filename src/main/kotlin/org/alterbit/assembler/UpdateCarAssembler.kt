package org.alterbit.assembler

import org.alterbit.commands.UpdateCarCommand
import org.alterbit.rest.UpdateCarRequest

class UpdateCarAssembler {

    fun requestToCommand(id: String, requestBody: UpdateCarRequest): UpdateCarCommand =
        UpdateCarCommand(id, requestBody.make, requestBody.colour)
}
