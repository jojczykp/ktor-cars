package org.alterbit.exceptions

class CreateCarException(val id: String) : Exception("Could not create car with id <$id>")
