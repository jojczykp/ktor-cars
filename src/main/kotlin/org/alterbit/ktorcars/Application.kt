package org.alterbit.ktorcars

import io.ktor.server.application.*
import org.alterbit.ktorcars.plugins.configureDI
import org.alterbit.ktorcars.plugins.configureRouting
import org.alterbit.ktorcars.plugins.configureSerialization
import org.alterbit.ktorcars.plugins.migrateDatabase

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    migrateDatabase()

    configureDI()
    configureSerialization()
    configureRouting()
}
