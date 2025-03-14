package it.gamma.service.idp.mongo.respositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.gamma.service.idp.mongo.model.OfficialLogData;

@Repository
public interface OfficialLogDataRepository extends MongoRepository<OfficialLogData, String> {

}
