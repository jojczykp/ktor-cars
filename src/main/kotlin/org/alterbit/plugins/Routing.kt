package org.alterbit.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.alterbit.assembler.CarResponseAssembler
import org.alterbit.assembler.CreateCarAssembler
import org.alterbit.assembler.UpdateCarAssembler
import org.alterbit.rest.CreateCarRequest
import org.alterbit.rest.UpdateCarRequest
import org.alterbit.services.CarsService
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.configureRouting() {

    routing {
        val carsService: CarsService by closestDI().instance()

        val createCarsAssembler: CreateCarAssembler by closestDI().instance()
        val updateCarsAssembler: UpdateCarAssembler by closestDI().instance()
        val carResponseAssembler: CarResponseAssembler by closestDI().instance()

        get("/cars") {
            val responseBody = carsService.getCars()
                .map { carResponseAssembler.carToResponse(it) }
            call.respond(responseBody)
        }

        get("/cars/{id}") {
            runCatching { carsService.getCar(call.parameters["id"]!!.toString()) }
                .onSuccess {
                    if (it.isFailure) {
                        call.respond(HttpStatusCode.NotFound)
                    } else {
                        val car = it.getOrThrow()
                        val responseBody = carResponseAssembler.carToResponse(car)
                        call.respond(responseBody)
                    }
                }
                .onFailure { call.respond(HttpStatusCode.NotFound) }
        }

        delete("/cars/{id}") {
            runCatching { carsService.deleteCar(call.parameters["id"]!!.toString()) }
                .onSuccess {
                    val deleted = it.getOrThrow()
                    call.respond(if (deleted) HttpStatusCode.NoContent else HttpStatusCode.NotFound)
                }
                .onFailure { call.respond(HttpStatusCode.NotFound) }
        }

        post("/cars") {
            val requestBody = call.receive<CreateCarRequest>()
            val command = createCarsAssembler.requestToCommand(requestBody)
            val newCar = carsService.createCar(command)
            newCar.onSuccess {
                val responseBody = carResponseAssembler.carToResponse(it)
                call.respond(HttpStatusCode.Created, responseBody)
            }
        }

        put("/cars/{id}") {
            runCatching { call.parameters["id"]!!.toString() }
                .onSuccess { id ->
                    val requestBody = call.receive<UpdateCarRequest>()
                    val command = updateCarsAssembler.requestToCommand(id, requestBody)
                    val updatedCar = carsService.updateCar(command)
                    updatedCar.onSuccess {
                        val responseBody = carResponseAssembler.carToResponse(it)
                        call.respond(HttpStatusCode.OK, responseBody)
                    }
                }
                .onFailure { call.respond(HttpStatusCode.NotFound) }
        }
    }
}
