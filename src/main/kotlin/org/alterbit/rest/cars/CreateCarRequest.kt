package org.alterbit.rest.cars

import kotlinx.serialization.Serializable

@Serializable
data class CreateCarRequest(
    val make: String,
    val colour: String
)
