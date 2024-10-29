package org.alterbit.ktorcars.exceptions

class CarNotFoundException(val id: String) : Exception("Car with id <$id> not found")
