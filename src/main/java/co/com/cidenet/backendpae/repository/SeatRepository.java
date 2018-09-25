package co.com.cidenet.backendpae.repository;

import co.com.cidenet.backendpae.model.Seat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface SeatRepository extends MongoRepository<Seat, String> {

    @Query("{'vehicle.numberplate':?0}")
    Seat findSeatByVehicleNumberPlate(String numberplate);
}
