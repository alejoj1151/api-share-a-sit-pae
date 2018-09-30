package co.com.cidenet.backendpae.controller;

import co.com.cidenet.backendpae.model.Vehicle;
import co.com.cidenet.backendpae.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/all")
    public List<Vehicle> getVehicles() {
        return vehicleService.getVehicles();
    }

    @GetMapping("/{id}")
    public Vehicle getVehicle(@PathVariable(name = "id") String id) {
        return vehicleService.getVehicleById(id);
    }

    @PostMapping
    public Vehicle saveVehicle(@RequestBody Vehicle newVehicle) {
        return vehicleService.saveVehicle(newVehicle);
    }
}
