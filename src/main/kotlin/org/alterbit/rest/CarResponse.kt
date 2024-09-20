package org.alterbit.rest

import kotlinx.serialization.Serializable

@Serializable
data class CarResponse(
    val id: Int,
    val make: String,
    val colour: String
)
