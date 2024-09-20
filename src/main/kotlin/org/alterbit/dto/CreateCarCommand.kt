package org.alterbit.dto

import org.alterbit.rest.CreateCarRequest

data class CreateCarCommand(
    val make: String,
    val colour: String
) {
    companion object {
        fun fromRequest(requestBody: CreateCarRequest): CreateCarCommand =
            CreateCarCommand(requestBody.make, requestBody.colour)
    }
}
