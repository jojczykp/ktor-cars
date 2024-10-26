package org.alterbit.exceptions

class CarNotFoundException(val id: String) : Exception("Car with id <$id> not found")
