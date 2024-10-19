package org.alterbit.dto

data class UpdateCarCommand(
    val id: String,
    val make: String? = null,
    val colour: String? = null
)
