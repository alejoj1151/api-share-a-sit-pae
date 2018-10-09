package co.com.cidenet.backendpae.service;

import co.com.cidenet.backendpae.model.Travel;
import co.com.cidenet.backendpae.model.User;
import co.com.cidenet.backendpae.model.Vehicle;
import co.com.cidenet.backendpae.repository.TravelRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TravelService {

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Travel getTravelByVehicleNumberPlate(String numberplate) {
        return travelRepository.findTravelByVehicleNumberPlate(numberplate);
    }

    public Travel updateTravel(String id_vehicle, String id_driver, Travel newTravel) throws ParseException {

        Vehicle vehicle = vehicleService.getVehicleById(id_vehicle);
        User driver = userService.getUserById(id_driver);

        if(vehicle != null && driver != null) {
            if(userService.IsVehicleTheUser(driver, vehicle)) {
                Travel travel = this.getTravelByVehicleNumberPlate(vehicle.getNumberplate());

                if (travel != null) {

                    newTravel.setId(travel.getId());
                    newTravel.setVehicle(vehicle);
                    newTravel.setDriver(driver);
                    if (newTravel.getOrigen().equalsIgnoreCase("volador")) {
                        newTravel.setDestination("minas");
                    } else if (newTravel.getOrigen().equalsIgnoreCase("minas")) {
                        newTravel.setDestination("volador");
                    }
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    newTravel.setDate(dateFormat.format(date));

                    DateFormat hourFormat = new SimpleDateFormat("HH:mm");
                    Date horaActual = hourFormat.parse(hourFormat.format(date));
                    Date horaTravel = hourFormat.parse(newTravel.getHour());

                    int diferencia = (int) ((horaTravel.getTime() - horaActual.getTime()) / 1000);

                    if (diferencia > 0) {
                        int horas = 0;
                        int minutos = 0;
                        if (Math.abs(diferencia) > 3600) {
                            horas = (int) Math.floor(diferencia / 3600);
                            diferencia = diferencia - (horas * 3600);
                        }
                        if (Math.abs(diferencia) > 60) {
                            minutos = (int) Math.floor(diferencia / 60);
                        }
                        System.out.println("INSERT: travel new have " + horas + " hour, and " + minutos + " minutes for start for the driver #" + driver.getDocument() + " in the vehicle #" + vehicle.getNumberplate());

                        if(travel.getPassengers() != null) {
                            List<User> listpassagers = new ArrayList<User>();
                            listpassagers.addAll(travel.getPassengers());
                            listpassagers.removeAll(listpassagers);
                            newTravel.setPassengers(listpassagers);
                        }
                        if(newTravel.getAvailable_seats() > vehicle.getTotalseats()) {
                            newTravel.setAvailable_seats(vehicle.getTotalseats());
                        }

                        travelRepository.save(newTravel);
                        //BeanUtils.copyProperties(newTravel, travel);
                        return newTravel;
                    } else {
                        System.out.println("ERROR: hour of the travel is invalid for the driver #" + driver.getDocument() + " (" + driver.getLastname() + " " + driver.getFirstname());
                    }
                }
                else {
                    System.out.println("ERROR: This travel of vehiculo not exist");
                }
            }
            else {
                System.out.println("ERROR: This vehicle is not of this user");
            }
        }
        return null;
    }

    public void deleteTravel(String numberplate) {
        Travel travel = getTravelByVehicleNumberPlate(numberplate);
        if(travel != null) {
            travelRepository.delete(travel);
        }
    }

    public Travel saveTravel(Travel newTravel) {
        Travel travel = getTravelByVehicleNumberPlate(newTravel.getVehicle().getNumberplate());
        if (travel == null) {
            travelRepository.save(newTravel);
            return travel;
        }
        return null;
    }

    public List<Travel> getTravelsAvailables() throws ParseException {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(date);

        List<Travel> travels = travelRepository.findTravelByDate(today);

        DateFormat hourFormat = new SimpleDateFormat("HH:mm");
        Date horaActual = hourFormat.parse(hourFormat.format(date));
        int diferencia = 0;
        for (int i = 0; i < travels.size(); i++) {
            Date horaTravel = hourFormat.parse(travels.get(i).getHour());
            diferencia = (int) ((horaTravel.getTime() - horaActual.getTime()) / 1000);
            if (diferencia < 0) {
                travels.remove(i);
            }
        }
        return travels;
    }

    public List<Travel> getTravelsForFilters(String origen, String hour1, String hour2) throws ParseException {

        Date date = new Date();

        DateFormat hourFormat = new SimpleDateFormat("HH:mm");
        Date horaInicio = hourFormat.parse(hour1);
        Date horaFinal = hourFormat.parse(hour2);

        Date horaActual = hourFormat.parse(hourFormat.format(date));

        int diferencia = (int) ((horaFinal.getTime() - horaInicio.getTime()) / 1000);

        int dif1 = (int) ((horaInicio.getTime() - horaActual.getTime()) / 1000);
        int dif2 = (int) ((horaFinal.getTime() - horaActual.getTime()) / 1000);

        if ((diferencia > 0) && (dif1 > 0 && dif2 > 0)) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String today = dateFormat.format(date);

            List<Travel> travels = travelRepository.findTravelOrigen(today, origen.toLowerCase());

            for (int i = 0; i < travels.size(); i++) {
                Date horaTravel = hourFormat.parse(travels.get(i).getHour());
                dif1 = (int) ((horaTravel.getTime() - horaInicio.getTime()) / 1000);
                dif2 = (int) ((horaFinal.getTime() - horaTravel.getTime()) / 1000);
                if (dif1 < 0 || dif2 < 0) {
                    travels.remove(i);
                }
            }

            return travels;

        }

        return null;
    }

    public boolean UserRegisterTravel(String id_vehicle, String id_passenger) {
        Vehicle vehicle = vehicleService.getVehicleById(id_vehicle);
        User passager = userService.getUserById(id_passenger);
        Travel travel = getTravelByVehicleNumberPlate(vehicle.getNumberplate());
        if(vehicle != null && passager != null && travel != null) {
            if (travel.getAvailable_seats() > 0) {
                List<User> listpassagers;
                if(travel.getPassengers() != null) {
                    listpassagers = travel.getPassengers();
                }
                else {
                    listpassagers = new ArrayList<User>();
                }
                listpassagers.add(passager);
                travel.setPassengers(listpassagers);
                travel.setAvailable_seats(travel.getAvailable_seats() - 1);
                travelRepository.save(travel);
                System.out.println("REGISTER: the user #" + passager.getDocument() + " (" + passager.getFirstname() + " " + passager.getLastname() + ") was register in the travel of vehicle #" + vehicle.getNumberplate());
                return true;
            }
            System.out.println("ERROR: the seats is not avaiables in the vehicle #" + vehicle.getNumberplate());
        }
        return false;
    }

    public boolean isUserRegisterInTravel(String id_passenger) throws ParseException {
        User passager = userService.getUserById(id_passenger);
        if(passager != null) {
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String today = dateFormat.format(date);

            List<Travel> travels = travelRepository.findIsUserTravelByDate(today);

            DateFormat hourFormat = new SimpleDateFormat("HH:mm");
            Date horaActual = hourFormat.parse(hourFormat.format(date));
            int diferencia = 0;
            for (int i = 0; i < travels.size(); i++) {
                Date horaTravel = hourFormat.parse(travels.get(i).getHour());
                diferencia = (int) ((horaTravel.getTime() - horaActual.getTime()) / 1000);
                if (diferencia > 0) {
                    List<User> list_passengers = travels.get(i).getPassengers();
                    if(list_passengers != null) {

                        // comprobar si alguno de los objetos de la lista <user> de pasajeros corresponde al objeto pasajero ingresado desde el front
                        for (int j = 0; j < list_passengers.size(); j++) {
                            String user_get_list_id = list_passengers.get(j).getId();
                            if (user_get_list_id.equals(id_passenger)) {
                                System.out.println("INFO: user #" + passager.getDocument() + " (" + passager.getFirstname() + " " + passager.getLastname() + ") is passenger in the travel of vehicle #" + travels.get(i).getVehicle().getNumberplate());
                                return true;
                            }
                        }
                    }
                }
            }
            System.out.println("INFO: User is not passenger of no travel");
            return false;
        }
        System.out.println("INFO: User not exits in the system");
        return false;
    }

    public boolean isUserDriverInTravel(String id_driver) throws ParseException {
        User driver = userService.getUserById(id_driver);
        if(driver != null) {

            if(driver.getVehicles() != null) {
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String today = dateFormat.format(date);

                List<Travel> travels = travelRepository.findIsUserTravelByDate(today);

                DateFormat hourFormat = new SimpleDateFormat("HH:mm");
                Date horaActual = hourFormat.parse(hourFormat.format(date));
                int diferencia = 0;
                for (int i = 0; i < travels.size(); i++) {
                    Date horaTravel = hourFormat.parse(travels.get(i).getHour());
                    diferencia = (int) ((horaTravel.getTime() - horaActual.getTime()) / 1000);
                    if (diferencia > 0) {
                        String id_driver_list = travels.get(i).getDriver().getId();

                        // comprobar si la identificación de algún conductor corresponde a la ingresada desde el front
                        if (id_driver_list.equals(id_driver)) {
                            System.out.println("INFO: user #" + driver.getDocument() + " (" + driver.getFirstname() + " " + driver.getLastname() + ") is driver in the travel of vehicle #" + travels.get(i).getVehicle().getNumberplate());
                            return true;
                        }
                    }
                }
            }
            System.out.println("INFO: User not has vehicles or travel registred");
            return false;
        }
        System.out.println("INFO: User not exits in the system");
        return false;
    }
}
