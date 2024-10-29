package org.alterbit.rest.cars

import org.alterbit.commands.CreateCarCommand
import org.alterbit.commands.UpdateCarCommand
import org.alterbit.model.Car

class CarsConverter {

    fun toCommand(request: CreateCarRequest): CreateCarCommand =
        CreateCarCommand(request.make, request.colour)

    fun toCommand(id: String, request: UpdateCarRequest): UpdateCarCommand =
        UpdateCarCommand(id, request.make, request.colour)

    fun toResponse(car: Car): CarResponse =
        CarResponse(car.id, car.make, car.colour)

}