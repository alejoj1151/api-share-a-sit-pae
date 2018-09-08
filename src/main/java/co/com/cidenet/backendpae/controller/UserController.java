package co.com.cidenet.backendpae.controller;

import co.com.cidenet.backendpae.model.User;
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

    @PostMapping
    public User saveUser(@RequestBody User newUser) {
        return userService.saveUser(newUser);
    }

}
