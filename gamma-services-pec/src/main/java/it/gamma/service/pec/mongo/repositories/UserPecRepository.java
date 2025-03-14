package it.gamma.service.pec.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.gamma.service.pec.mongo.model.UserPec;

@Repository
public interface UserPecRepository extends MongoRepository<UserPec, String> {

}
