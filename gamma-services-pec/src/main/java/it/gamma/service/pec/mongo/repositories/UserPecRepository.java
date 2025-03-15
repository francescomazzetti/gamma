package it.gamma.service.pec.mongo.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import it.gamma.service.pec.mongo.model.UserPec;

@Repository
public interface UserPecRepository extends MongoRepository<UserPec, String> {

	@Query("{ 'taxcode' : ?0 }")
	List<UserPec> findByTaxcode(String taxcode);
}
