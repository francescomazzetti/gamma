package it.gamma.service.signer.mongo.respository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.gamma.service.signer.mongo.model.OfficialLogData;

@Repository
public interface OfficialLogDataRepository extends MongoRepository<OfficialLogData, String> {

}
