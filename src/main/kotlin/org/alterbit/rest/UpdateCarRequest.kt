package org.alterbit.rest

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCarRequest(
    val make: String? = null,
    val colour: String? = null
)
