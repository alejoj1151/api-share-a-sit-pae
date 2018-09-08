package co.com.cidenet.backendpae.repository;

import co.com.cidenet.backendpae.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VehicleRepository extends MongoRepository<Vehicle, Integer> {
    List<Vehicle> findAll();
    Vehicle findVehicleById(String id);
    Vehicle findVehicleByNumberplate(String number_plate);
}

