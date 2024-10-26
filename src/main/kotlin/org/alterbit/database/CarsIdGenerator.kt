package org.alterbit.database

import java.util.*

class CarsIdGenerator {
    fun newId(): String = UUID.randomUUID().toString()
}
