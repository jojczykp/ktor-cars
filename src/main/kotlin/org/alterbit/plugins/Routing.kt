package org.alterbit.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.alterbit.dto.CreateCarCommand
import org.alterbit.dto.UpdateCarCommand
import org.alterbit.rest.CreateCarRequest
import org.alterbit.rest.UpdateCarRequest
import org.alterbit.services.CarsService
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {
        val carsService: CarsService by inject()

        get("/cars") {
            call.respond(carsService.getCars())
        }

        get("/cars/{id}") {
            runCatching { carsService.getCar(call.parameters["id"]!!.toInt()) }
                .onSuccess { car -> call.respond(car.getOrThrow()) }
                .onFailure { call.respond(HttpStatusCode.NotFound) }
        }

        delete("/cars/{id}") {
            runCatching { carsService.deleteCar(call.parameters["id"]!!.toInt()) }
                .onSuccess {
                    val deleted = it.getOrThrow()
                    call.respond(if (deleted) HttpStatusCode.NoContent else HttpStatusCode.NotFound)
                }
                .onFailure { call.respond(HttpStatusCode.NotFound) }
        }

        post("/cars") {
            val requestBody = call.receive<CreateCarRequest>()
            val command = CreateCarCommand.fromRequest(requestBody)
            val newCar = carsService.createCar(command)
            newCar.onSuccess { call.respond(HttpStatusCode.Created, it) }
        }

        put("/cars/{id}") {
            runCatching { call.parameters["id"]!!.toInt() }
                .onSuccess { id ->
                    val requestBody = call.receive<UpdateCarRequest>()
                    val command = UpdateCarCommand.fromRequest(id, requestBody)
                    val updatedCar = carsService.updateCar(command)
                    updatedCar.onSuccess { call.respond(HttpStatusCode.OK, it) }

                }
                .onFailure { call.respond(HttpStatusCode.NotFound) }
        }
    }
}
