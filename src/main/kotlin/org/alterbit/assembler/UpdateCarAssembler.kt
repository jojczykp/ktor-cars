package org.alterbit.assembler

import org.alterbit.dto.UpdateCarCommand
import org.alterbit.rest.UpdateCarRequest

class UpdateCarAssembler {

    fun requestToCommand(id: Int, requestBody: UpdateCarRequest): UpdateCarCommand =
        UpdateCarCommand(id, requestBody.make, requestBody.colour)
}
