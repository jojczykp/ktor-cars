package org.alterbit.assembler

import org.alterbit.model.Car
import org.alterbit.rest.CarResponse

class CarResponseAssembler {

    fun carToResponse(car: Car): CarResponse =
        CarResponse(car.id, car.make, car.colour)
}