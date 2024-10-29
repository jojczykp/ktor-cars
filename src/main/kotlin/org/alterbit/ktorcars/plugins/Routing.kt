package org.alterbit.ktorcars.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.alterbit.ktorcars.rest.Cars
import org.alterbit.ktorcars.rest.CarsConverter
import org.alterbit.ktorcars.rest.requests.CreateCarRequest
import org.alterbit.ktorcars.rest.requests.UpdateCarRequest
import org.alterbit.ktorcars.services.CarsService
import org.alterbit.ktorcars.utils.withMdc
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.util.*

fun Application.configureRouting() {
    install(Resources)

    routing {
        val carsService: CarsService by closestDI().instance()
        val carsConverter: CarsConverter by closestDI().instance()

        get<Cars> {
            withMdc(requestFields()) {
                carsService.getCars()
                    .map { cars -> carsConverter.toResponse(cars) }
                    .let { responseBody -> call.respond(HttpStatusCode.OK, responseBody) }
            }
        }

        get<Cars.Id> {
            withMdc(requestFields()) {
                carsService.getCar(it.id)
                    ?.let { car -> carsConverter.toResponse(car) }
                    ?.let { responseBody -> call.respond(HttpStatusCode.OK, responseBody) }
                    ?: call.respond(HttpStatusCode.NotFound)
            }
        }

        delete<Cars.Id> {
            withMdc(requestFields()) {
                carsService.deleteCar(it.id)
                    ?.let { car -> carsConverter.toResponse(car) }
                    ?.let { responseBody -> call.respond(HttpStatusCode.OK, responseBody) }
                    ?: call.respond(HttpStatusCode.NotFound)
            }
        }

        post<Cars> {
            withMdc(requestFields()) {
                call.receive<CreateCarRequest>()
                    .let { requestBody -> carsConverter.toCommand(requestBody) }
                    .let { command -> carsService.createCar(command) }
                    .let { car -> carsConverter.toResponse(car) }
                    .let { responseBody -> call.respond(HttpStatusCode.Created, responseBody) }
            }
        }

        put<Cars.Id> { request ->
            withMdc(requestFields()) {
                call.receive<UpdateCarRequest>()
                    .let { requestBody -> carsConverter.toCommand(request.id, requestBody) }
                    .let { command -> carsService.updateCar(command) }
                    ?.let { car -> carsConverter.toResponse(car) }
                    ?.let { responseBody -> call.respond(HttpStatusCode.OK, responseBody) }
                    ?: call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

private fun requestFields() = mapOf(
    "requestId" to UUID.randomUUID().toString()
)
