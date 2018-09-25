package co.com.cidenet.backendpae.service;

import co.com.cidenet.backendpae.model.Seat;
import co.com.cidenet.backendpae.model.Vehicle;
import co.com.cidenet.backendpae.repository.SeatRepository;
import co.com.cidenet.backendpae.repository.VehicleRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatService seatService;

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
        Vehicle veh1 = getVehicleByNumberplate(newVehicle.getNumberplate());
        if(veh1 == null) {
            vehicleRepository.save(newVehicle);
            //Seat seat = new Seat(newVehicle, null, newVehicle.getTotalseats(), null, null, null);
            //seatRepository.save(seat);
            return newVehicle;
        }

        return null;
    }

    public void deleteVehicle(String numberplate) {
        if(getVehicleByNumberplate(numberplate) != null) {
            vehicleRepository.delete(getVehicleByNumberplate(numberplate));
            seatService.deleteSeat(numberplate);
        }
    }
}
