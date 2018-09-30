package co.com.cidenet.backendpae.service;

import co.com.cidenet.backendpae.model.Travel;
import co.com.cidenet.backendpae.model.Vehicle;
import co.com.cidenet.backendpae.repository.TravelRepository;
import co.com.cidenet.backendpae.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private TravelService travelService;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Vehicle> getVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(String id) {
        return vehicleRepository.findVehicleById(id);
    }

    public Vehicle getVehicleByNumberplate(String numberplate) {
        return vehicleRepository.findVehicleByNumberplate(numberplate);
    }

    public Vehicle saveVehicle(Vehicle newVehicle) {
        Vehicle veh = getVehicleByNumberplate(newVehicle.getNumberplate());
        if(veh == null) {
            if(!newVehicle.getType().equalsIgnoreCase("carro") && !newVehicle.getType().equalsIgnoreCase("moto")  && !newVehicle.getType().equalsIgnoreCase("camioneta")) {
                newVehicle.setType("Otro");
            }
            newVehicle.setNumberplate(newVehicle.getNumberplate().toUpperCase());
            vehicleRepository.save(newVehicle);
            Travel travel = new Travel(newVehicle, newVehicle.getTotalseats());
            travelService.saveTravel(travel);
            System.out.println("INSERT: vehicle #" + newVehicle.getNumberplate() + " registred his travel object");
            return newVehicle;
        }

        return null;
    }

    public void deleteVehicle(String numberplate) {
        if(getVehicleByNumberplate(numberplate) != null) {
            vehicleRepository.delete(getVehicleByNumberplate(numberplate));
            travelService.deleteTravel(numberplate);
            System.out.println("DELETE: vehicle #" + numberplate + " deleted his travel object");
        }
    }

    public void setTravelDefault(Vehicle vehicle) {
        Travel travel = travelService.getTravelByVehicleNumberPlate(vehicle.getNumberplate());
        if(travel != null) {
            travel.setAvailable_seats(vehicle.getTotalseats());
            travel.setVehicle(vehicle);

            // update data in collection
            mongoTemplate.save(travel, "travels");

            System.out.println("UPDATE: vehicle #" + vehicle.getNumberplate() + " updated his travel object");
        }
    }
}
