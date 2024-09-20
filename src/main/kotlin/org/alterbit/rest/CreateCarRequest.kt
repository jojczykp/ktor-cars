package org.alterbit.rest

import kotlinx.serialization.Serializable

@Serializable
data class CreateCarRequest(
    val make: String,
    val colour: String
)
