package com.meliora.natujenge.repositories;

import com.meliora.natujenge.annotations.Repository;
import com.meliora.natujenge.models.CarModel;

import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CarRepository {
    AtomicInteger numberOfRequests = new AtomicInteger(0);

    public CarRepository() {
        System.out.println("Initializing car repository");
    }

    public boolean save(CarModel carModel) {
        numberOfRequests.incrementAndGet();

        System.out.println("Repository --- Request number " + numberOfRequests + " to save to db " + carModel);
        return true;
    }
}
