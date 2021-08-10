package com.meliora.natujenge.services;

import com.meliora.natujenge.annotations.Autowired;
import com.meliora.natujenge.annotations.Service;
import com.meliora.natujenge.models.CarModel;
import com.meliora.natujenge.repositories.CarRepository;

@Service
public class CarService {

    @Autowired
    public CarRepository carRepository;

    public void save(CarModel carModel) {
        System.out.println("Reached service layer ");

        carRepository.save(carModel);
    }

}
