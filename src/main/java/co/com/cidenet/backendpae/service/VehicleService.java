package co.com.cidenet.backendpae.service;

import co.com.cidenet.backendpae.model.Vehicle;
import co.com.cidenet.backendpae.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(String id) {
        return vehicleRepository.findVehicleById(id);
    }

    public Vehicle getVehicleByNumber_plate(String number_plate) {
        return vehicleRepository.findVehicleByNumberplate(number_plate);
    }

    public Vehicle saveVehicle(Vehicle newVehicle) {
        vehicleRepository.save(newVehicle);
        return newVehicle;
    }
}
