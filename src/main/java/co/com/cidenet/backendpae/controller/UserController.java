package co.com.cidenet.backendpae.controller;

import co.com.cidenet.backendpae.model.User;
import co.com.cidenet.backendpae.model.Vehicle;
import co.com.cidenet.backendpae.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public List<User> getUsers() {
        return userService.getUsers();

    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable(name = "id") String id) {
        return userService.getUserById(id);
    }

    @GetMapping(value = "/search", params = {"document"})
    public User getUserByDocument(@RequestParam(name = "document") int document) {
        return userService.getUserByDocument(document);
    }

    @GetMapping(value = "/search", params = {"email"})
    public User getUserByEmail(@RequestParam(name = "email") String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping( value = "/login",
                 params = { "email", "password" })
    public User getUserLogin(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password) {
        return userService.loginUser(email, password);
    }

    @GetMapping(value = "/vehicles", params = {"document"})
    public List<Vehicle> getVehiclesByUser(@RequestParam(name = "document") int document) {
        return userService.getVehiclesByUser(document);
    }

    @PostMapping
    public User saveUser(@RequestBody User newUser) {
        return userService.saveUser(newUser);
    }

    @PutMapping("/add-vehicles/{id}")
    public User updateStudent(@PathVariable(name = "id") String id, @RequestBody User newUser) {
        newUser.setId(id);
        return userService.updateUser(newUser);
    }

    @DeleteMapping( value = "/delete-vehicles",
                    params = { "document", "numberplate" })
    public String deleteVehicle(@RequestParam(name = "document") int document, @RequestParam(name = "numberplate") String numberplate) {
        return userService.deleteVehicle(document, numberplate);
    }

}
