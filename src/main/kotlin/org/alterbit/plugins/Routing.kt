package org.alterbit.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.alterbit.services.CarsService
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    install(AutoHeadResponse)

    routing {
        val carsService: CarsService by inject()

        get("/cars") {
            call.respond(carsService.getCars())
        }

        get("/cars/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val car = carsService.getCar(id)

            if (car != null) {
                call.respond(car)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
