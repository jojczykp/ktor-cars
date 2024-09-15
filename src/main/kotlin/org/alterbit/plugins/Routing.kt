package org.alterbit.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.alterbit.services.CarsService
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {
        val carsService: CarsService by inject()

        get("/cars") {
            call.respond(carsService.getCars())
        }

        get("/cars/{id}") {
            call.parameters["id"]!!.toInt()
                .let { id -> carsService.getCar(id) }
                .onSuccess { car -> call.respond(car) }
                .onFailure { call.respond(HttpStatusCode.NotFound) }
        }
    }
}
