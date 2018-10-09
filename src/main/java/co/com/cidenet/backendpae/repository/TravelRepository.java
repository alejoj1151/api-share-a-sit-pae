package co.com.cidenet.backendpae.repository;

import co.com.cidenet.backendpae.model.Travel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TravelRepository extends MongoRepository<Travel, String> {

    @Query("{'vehicle.numberplate':?0}")
    Travel findTravelByVehicleNumberPlate(String numberplate);

    @Query("{'date':?0, 'available_seats':{'$gt':0}}")
    List<Travel> findTravelByDate(String date);

    @Query("{'date':?0, 'origen':?1, 'available_seats':{'$gt':0}}")
    List<Travel> findTravelOrigen(String date, String origen);

    //query para comprobar si se es conductor o pasajero de un viaje activo
    @Query("{'date':?0}")
    List<Travel> findIsUserTravelByDate(String date);
}
