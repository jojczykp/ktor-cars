package org.alterbit.dto

import org.alterbit.rest.UpdateCarRequest

data class UpdateCarCommand(
    val id: Int,
    val make: String? = null,
    val colour: String? = null
) {
    companion object {
        fun fromRequest(id: Int, requestBody: UpdateCarRequest): UpdateCarCommand =
            UpdateCarCommand(id, requestBody.make, requestBody.colour)
    }
}
