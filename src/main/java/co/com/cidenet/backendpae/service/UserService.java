package co.com.cidenet.backendpae.service;

import co.com.cidenet.backendpae.model.User;
import co.com.cidenet.backendpae.model.Vehicle;
import co.com.cidenet.backendpae.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findUserById(id);
    }

    public User getUserByDocument(long document) {
        return userRepository.findUserByDocument(document);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public List<User> getUsersByName(String name) {
        return userRepository.findUsersByFirstname(name);
    }

    public boolean saveUser(User newUser) {
        User user1 = getUserByDocument(newUser.getDocument());
        User user2 = getUserByEmail(newUser.getEmail());
        if(user1 == null && user2 == null) {
            userRepository.save(newUser);
            System.out.println("INSERT: user #" + newUser.getDocument() + " (" + newUser.getFirstname() + " " + newUser.getLastname() + ") was inserted");
            return true;
        }
        return false;
    }

    public User updateUser(User newUser) {
        User user = this.getUserById(newUser.getId());

        if(user != null) {

            if(newUser.getVehicles() != null) {

                Enumeration<Vehicle> e = Collections.enumeration(newUser.getVehicles());

                if (e != null) {
                    List<Vehicle> listVehicles = new ArrayList<Vehicle>();
                    while (e.hasMoreElements()) {
                        Vehicle vehicle = e.nextElement();

                        // Si el vehiculo no existe, crear el objeto
                        if (vehicleService.getVehicleByNumberplate(vehicle.getNumberplate()) == null) {
                            listVehicles.add(vehicleService.saveVehicle(vehicle));
                            System.out.println("INSERT: user #" + user.getDocument() + " (" + user.getFirstname() + " " + user.getLastname() + ") inserted the vehicle #" + vehicle.getNumberplate());

                        // Si el vehiculo existe, actualizar la entidad
                        } else {
                            vehicle.setId(vehicleService.getVehicleByNumberplate(vehicle.getNumberplate()).getId());

                            // update data in collection
                            if(!vehicle.getType().equalsIgnoreCase("carro") && !vehicle.getType().equalsIgnoreCase("moto")  && !vehicle.getType().equalsIgnoreCase("camioneta")) {
                                vehicle.setType("Otro");
                            }
                            vehicle.setNumberplate(vehicle.getNumberplate().toUpperCase());
                            mongoTemplate.save(vehicle, "vehicles");

                            vehicleService.setTravelDefault(vehicle);
                            //comprobar si el usuario ya tiene el vehiculo agregado, para evitar duplicados
                            if(user.getVehicles() != null) {
                                for (int i = 0; i < user.getVehicles().size(); i++) {
                                    if (user.getVehicles().get(i).getNumberplate().trim().equals(vehicle.getNumberplate())) {
                                        List<Vehicle> aux_list = user.getVehicles();
                                        aux_list.remove(i);
                                        user.setVehicles(aux_list);
                                    }
                                }
                            }
                            listVehicles.add(vehicle);
                            System.out.println("UPDATE: user #" + user.getDocument() + " (" + user.getFirstname() + " " + user.getLastname() + ") updated the vehicle #" + vehicle.getNumberplate());
                        }
                    }
                    if (user.getVehicles() != null) {
                        listVehicles.addAll(user.getVehicles());
                    }
                    newUser.setVehicles(listVehicles);
                    user.setVehicles(listVehicles);
                    //Query searchQuery = new Query(where("document").is(user.getDocument()));
                    //mongoTemplate.updateFirst(searchQuery, Update.update("vehicles", newUser.getVehicles()), User.class);
                }
            }

            // update data in collection
            userRepository.save(user);

            System.out.println("UPDATE: information of user #" + user.getDocument() + " (" + user.getFirstname() + ") was updated");
        }
        return user;
    }

    public User loginUser(String email, String password) {
        User user = userRepository.finUserAndPassword(email, password);

        if(user != null) {
            return user;
        }
        return null;
    }

    public String deleteVehicle(String id, String numberplate) {
        User user = this.getUserById(id);
        if(user != null) {
            User newUser = new User();
            newUser.setId(user.getId());
            if(user.getVehicles() != null && vehicleService.getVehicleByNumberplate(numberplate) != null) {
                Vehicle vehicle = vehicleService.getVehicleByNumberplate(numberplate);
                List<Vehicle> listVeh = new ArrayList<Vehicle>();
                listVeh.addAll(user.getVehicles());

                for(int i = 0; i < listVeh.size(); i++) {
                    if(listVeh.get(i).getNumberplate().trim().equals(numberplate)){
                        listVeh.remove(i);
                        break;
                    }
                }
                user.setVehicles(listVeh);

                // update data in collection
                userRepository.save(user);

                System.out.println("UPDATE: user #" + user.getDocument() + " (" + user.getFirstname() + " " + user.getLastname() + ") delete the vehicle #" + vehicle.getNumberplate());

                // Consulta para obtener la identificación de la referencia del vehículo almacenado en la colección vehículos
                Query query = new Query(Criteria.where("vehicles.$id").is(new ObjectId(vehicle.getId())));
                List<User> listUser = mongoTemplate.find(query, User.class);
                // End query

                if(listUser.size() == 0) {
                    vehicleService.deleteVehicle(numberplate);
                    System.out.println("DELETE: the vehicle #" + vehicle.getNumberplate() + " was deleted of the database");
                }
                return "The Vehicle was deleted successfully.";
            }
        }
        return "Error in the deletion of vehicle.";
    }

    public List<Vehicle> getVehiclesByUser(String id) {
        User user = this.getUserById(id);
        if(user != null) {
            List<Vehicle> listVeh = new ArrayList<Vehicle>();
            listVeh.addAll(user.getVehicles());
            return listVeh;
        }
        return null;
    }

    public boolean IsVehicleTheUser(User user, Vehicle vehicle) {
        if(user.getVehicles() != null) {
            for (int i = 0; i < user.getVehicles().size(); i++) {
                if (user.getVehicles().get(i).getNumberplate().trim().equals(vehicle.getNumberplate())) {
                    return true;
                }
            }
        }
        return false;
    }
}
