package org.alterbit.ktorcars.database.cars

import java.util.*

class CarsIdGenerator {
    fun newId(): String = UUID.randomUUID().toString()
}
