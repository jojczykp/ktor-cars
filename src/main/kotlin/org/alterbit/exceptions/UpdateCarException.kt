package org.alterbit.exceptions

class UpdateCarException(val id: String) : Exception("Could not update car with id <$id>")
