package it.gamma.service.worker.mongo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import it.gamma.service.worker.mongo.model.SignedAttachmentModel;

@Repository
public interface SignedAttachmentRepository extends MongoRepository<SignedAttachmentModel, String> {
	
	@Query("{'status': ?0}")
	public List<SignedAttachmentModel> findByStatus(String status, Pageable pageable);
	
}
