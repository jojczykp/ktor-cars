package org.alterbit.plugins

import io.ktor.server.application.*
import org.alterbit.carsModule
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(carsModule)
    }
}
