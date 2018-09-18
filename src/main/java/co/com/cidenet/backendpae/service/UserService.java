package co.com.cidenet.backendpae.service;

import co.com.cidenet.backendpae.model.User;
import co.com.cidenet.backendpae.model.Vehicle;
import co.com.cidenet.backendpae.repository.UserRepository;
import co.com.cidenet.backendpae.repository.VehicleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findUserById(id);
    }

    public User getUserByDocument(int document) {
        return userRepository.findUserByDocument(document);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public List<User> getUsersByName(String name) {
        return userRepository.findUsersByFirstname(name);
    }

    public User saveUser(User newUser) {
        User user1 = getUserByDocument(newUser.getDocument());
        User user2 = getUserByEmail(newUser.getEmail());
        if(user1 == null && user2 == null) {
            userRepository.save(newUser);
            return newUser;
        }
        return null;
    }

    public User updateStudent(User newUser) {
        User user = this.getUserById(newUser.getId());

        if(user != null) {

            Enumeration<Vehicle> e = Collections.enumeration(newUser.getVehicles());
            if(e != null) {
                while(e.hasMoreElements())
                {
                    Vehicle vehicle = e.nextElement();
                    vehicleRepository.save(vehicle);
                }
                newUser.getVehicles().addAll(user.getVehicles());
                Query searchQuery = new Query(Criteria.where("document").is(user.getDocument()));
                mongoTemplate.updateFirst(searchQuery, Update.update("vehicles", newUser.getVehicles()), User.class);
            }

            BeanUtils.copyProperties(newUser, user);
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
}
