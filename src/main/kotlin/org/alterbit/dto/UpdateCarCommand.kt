package org.alterbit.dto

data class UpdateCarCommand(
    val id: Int,
    val make: String? = null,
    val colour: String? = null
)
