package co.com.cidenet.backendpae.repository;

import co.com.cidenet.backendpae.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User,String> {
    List<User> findAll();
    User findUserById(String id);
    User findUserByDocument(long document);
    List<User> findUsersByFirstname(String firstname);
    User findUserByEmail(String email);

    @Query("{'email' : ?0 , 'password' : ?1}")
    User findUserAndPassword(String email, String password);
}
