package co.com.cidenet.backendpae.service;

import co.com.cidenet.backendpae.model.User;
import co.com.cidenet.backendpae.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;



    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findUserById(id);
    }

    public User getUserByDocument(int document) {
        return userRepository.findUserByDocument(document);
    }

    public List<User> getUsersByName(String name) {
        return userRepository.findUsersByFirstname(name);
    }

    public User saveUser(User newUser) {
        userRepository.save(newUser);
        return newUser;
    }
}
