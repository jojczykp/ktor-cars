package org.alterbit.rest.cars

import org.alterbit.commands.CreateCarCommand
import org.alterbit.commands.UpdateCarCommand
import org.alterbit.model.Car

class CarRestConverter {

    fun requestToCommand(request: CreateCarRequest): CreateCarCommand =
        CreateCarCommand(request.make, request.colour)

    fun requestToCommand(id: String, request: UpdateCarRequest): UpdateCarCommand =
        UpdateCarCommand(id, request.make, request.colour)

    fun modelToResponse(car: Car): CarResponse =
        CarResponse(car.id, car.make, car.colour)

}