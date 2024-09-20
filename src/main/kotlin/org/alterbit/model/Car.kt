package org.alterbit.model

import kotlinx.serialization.Serializable

@Serializable
data class Car(
    val id: Int,
    val make: String,
    val colour: String
)
