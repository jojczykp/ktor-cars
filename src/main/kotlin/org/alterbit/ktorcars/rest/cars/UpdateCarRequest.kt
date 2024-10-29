package org.alterbit.ktorcars.rest.cars

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCarRequest(
    val make: String? = null,
    val colour: String? = null
)
