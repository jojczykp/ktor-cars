package org.alterbit.database.cars

import java.util.*

class CarsIdGenerator {
    fun newId(): String = UUID.randomUUID().toString()
}
