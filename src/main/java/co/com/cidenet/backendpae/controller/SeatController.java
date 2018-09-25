package co.com.cidenet.backendpae.controller;


import co.com.cidenet.backendpae.model.Seat;
import co.com.cidenet.backendpae.service.SeatService;
import co.com.cidenet.backendpae.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @Autowired
    private VehicleService vehicleService;

    @PutMapping("/add-seat/{id}") //id is the vehicle
    public Seat updateStudent(@PathVariable(name = "id") String id_vehicle, @RequestBody Seat newSeat) {
        return seatService.updateSeat(id_vehicle, newSeat);
    }
}
