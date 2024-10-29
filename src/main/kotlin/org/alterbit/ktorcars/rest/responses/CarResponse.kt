package org.alterbit.ktorcars.rest.responses

import kotlinx.serialization.Serializable

@Serializable
data class CarResponse(
    val id: String,
    val make: String,
    val colour: String
)
