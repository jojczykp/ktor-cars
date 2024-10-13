package org.alterbit

import io.ktor.server.application.*
import org.alterbit.plugins.configureDI
import org.alterbit.plugins.configureRouting
import org.alterbit.plugins.configureSerialization

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDI()
    configureSerialization()
    configureRouting()
}
