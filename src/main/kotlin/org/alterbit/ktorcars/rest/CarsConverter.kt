package org.alterbit.ktorcars.rest

import org.alterbit.ktorcars.commands.CreateCarCommand
import org.alterbit.ktorcars.commands.UpdateCarCommand
import org.alterbit.ktorcars.model.Car
import org.alterbit.ktorcars.rest.requests.CreateCarRequest
import org.alterbit.ktorcars.rest.requests.UpdateCarRequest
import org.alterbit.ktorcars.rest.responses.CarResponse

class CarsConverter {

    fun toCommand(request: CreateCarRequest): CreateCarCommand =
        CreateCarCommand(request.make, request.colour)

    fun toCommand(id: String, request: UpdateCarRequest): UpdateCarCommand =
        UpdateCarCommand(id, request.make, request.colour)

    fun toResponse(car: Car): CarResponse =
        CarResponse(car.id, car.make, car.colour)
}