package com.meliora.natujenge.crons;

import com.meliora.natujenge.annotations.Autowired;
import com.meliora.natujenge.annotations.Helper;
import com.meliora.natujenge.models.CarModel;
import com.meliora.natujenge.services.CarService;

@Helper
public class CarHelper implements Runnable {

    @Autowired
    public CarService carService;

    public Thread thread;

    public void init() {
        System.out.println("Starting car helper thread...");

        thread = new Thread(this, "CarHelper");
        thread.start();

        System.out.println("Started the car helper thread successfully...");
    }

    public void run() {
        while (true) {
            try {
                System.out.println("About to save ");
                CarModel carModel = new CarModel();
                carModel.setModel("Subaru");
                carModel.setColor("black");
                carModel.setEngineSize(1500);
                carModel.setNumberPlate("KDE 234F");

                carService.save(carModel);

                Thread.sleep(30000L);
            } catch (InterruptedException e) {
                System.out.println("Exception " + e.getMessage());
            }


        }
    }
}
