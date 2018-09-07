package co.com.cidenet.backendpae.dao;

import co.com.cidenet.backendpae.model.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<Users,String> {
    Users findById(ObjectId id);

}
