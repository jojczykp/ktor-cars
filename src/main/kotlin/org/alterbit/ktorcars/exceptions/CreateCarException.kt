package org.alterbit.ktorcars.exceptions

class CreateCarException(val id: String) : Exception("Could not create car with id <$id>")
