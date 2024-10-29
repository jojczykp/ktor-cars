package org.alterbit.ktorcars.rest

import io.ktor.resources.*

@Resource("/cars")
class Cars {

    @Resource("{id}")
    class Id(
        val parent: Cars = Cars(),
        val id: String
    )
}
