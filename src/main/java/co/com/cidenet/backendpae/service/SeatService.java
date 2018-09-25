package co.com.cidenet.backendpae.service;

import co.com.cidenet.backendpae.model.Seat;
import co.com.cidenet.backendpae.model.Vehicle;
import co.com.cidenet.backendpae.repository.SeatRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private VehicleService vehicleService;

    public Seat getSeatByVehicleNumberPlate(String numberplate) {
        return seatRepository.findSeatByVehicleNumberPlate(numberplate);
    }

    public Seat updateSeat(String id_vehicle, Seat newSeat) {

        Vehicle vehicle = vehicleService.getVehicleById(id_vehicle);

        Seat seat = this.getSeatByVehicleNumberPlate(vehicle.getNumberplate());
        newSeat.setId(seat.getId());
        if(seat != null) {
            BeanUtils.copyProperties(newSeat, seat);
        }

        return seat;
    }

    public void deleteSeat(String numberplate) {
        Seat seat = getSeatByVehicleNumberPlate(numberplate);
        if(seat != null) {
            seatRepository.delete(seat);
        }
    }
}
