package co.com.cidenet.backendpae.controller;


import co.com.cidenet.backendpae.model.Travel;
import co.com.cidenet.backendpae.model.User;
import co.com.cidenet.backendpae.service.TravelService;
import co.com.cidenet.backendpae.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/travels")
public class TravelController {

    @Autowired
    private TravelService travelService;

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/all")
    public List<Travel> getTravels() throws ParseException {
        return travelService.getTravelsAvailables();
    }

    @GetMapping( value = "/isdriver",
            params = { "id" })
    public boolean isDriverInTravel(@RequestParam(name = "id") String id_driver) throws ParseException {
        return travelService.isUserDriverInTravel(id_driver);
    }

    @GetMapping( value = "/ispassenger",
            params = { "id" })
    public boolean isPassengerInTravel(@RequestParam(name = "id") String id_passenger) throws ParseException {
        return travelService.isUserRegisterInTravel(id_passenger);
    }

    @GetMapping( value = "/listpassengers",
            params = { "id" })
    public List<User> getPassengersForTravel(@RequestParam(name = "id") String id_driver) throws ParseException {
        return travelService.getPassengerForTravelByIdDriver(id_driver);
    }

    @GetMapping( value = "/search",
            params = { "origen", "hourstart", "hourend" })
    public List<Travel> getTravelsFilter(@RequestParam(name = "origen") String origen, @RequestParam(name = "hourstart") String hourstart, @RequestParam(name = "hourend") String hourend) throws ParseException {
        return travelService.getTravelsForFilters(origen, hourstart, hourend);
    }

    @PutMapping( value = "/insert",
            params = { "vehicleid", "driverid" })
    public Travel updateTravel(@RequestParam(name = "vehicleid") String id_vehicle, @RequestParam(name = "driverid") String id_driver, @RequestBody Travel newTravel) throws ParseException {
        return travelService.updateTravel(id_vehicle, id_driver, newTravel);
    }

    @PutMapping( value = "/register",
            params = { "vehicleid", "passagerid" })
    public boolean UserRegisterTravel(@RequestParam(name = "vehicleid") String id_vehicle, @RequestParam(name = "passagerid") String id_passagerid) {
        return travelService.UserRegisterTravel(id_vehicle, id_passagerid);
    }
}
