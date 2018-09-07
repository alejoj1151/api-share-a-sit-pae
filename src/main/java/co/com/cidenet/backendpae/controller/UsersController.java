package co.com.cidenet.backendpae.controller;

import co.com.cidenet.backendpae.dao.UsersRepository;
import co.com.cidenet.backendpae.model.Users;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersRepository repository;

    //GET
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public List<Users> getAllUsers(){
        return repository.findAll();
    }

    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public Users getUserById(@PathVariable("id")ObjectId id){
        return repository.findById(id);
    }

    //PUT

    @RequestMapping(value="/{id}",method = RequestMethod.PUT)
    public void modifyUserById(@PathVariable("id") ObjectId id, @Valid @RequestBody Users users){
        users.setId(id);
        repository.save(users);
    }

    //POST

    @RequestMapping(value = "/",method = RequestMethod.POST)
    public Users createUser(@Valid @RequestBody Users users){
        users.setId(ObjectId.get());
        repository.save(users);
        return users;
    }

    //DELETE
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable ObjectId id){
        repository.delete (repository.findById(id));

    }

}
