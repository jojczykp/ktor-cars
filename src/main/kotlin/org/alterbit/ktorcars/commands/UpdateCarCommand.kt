package org.alterbit.ktorcars.commands

data class UpdateCarCommand(
    val id: String,
    val make: String? = null,
    val colour: String? = null
)
