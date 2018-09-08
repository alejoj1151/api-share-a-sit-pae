package co.com.cidenet.backendpae.repository;

import co.com.cidenet.backendpae.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User,String> {
    List<User> findAll();
    User findUserById(String id);
    User findUserByDocument(int document);
    List<User> findUsersByFirstname(String firstname);

}
