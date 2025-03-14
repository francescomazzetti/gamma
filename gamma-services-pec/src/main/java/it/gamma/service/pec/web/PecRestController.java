package it.gamma.service.pec.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.gamma.service.pec.mongo.repositories.UserPecRepository;

@RestController
public class PecRestController
{
	private UserPecRepository _userPecRepository;
	private Logger log;
	private MongoTemplate _mongoTemplate;

	@Autowired
	public PecRestController(
			UserPecRepository userPecRepository,
			MongoTemplate mongoTemplate
			) {
		_userPecRepository = userPecRepository;
		_mongoTemplate = mongoTemplate;
		log = LoggerFactory.getLogger(PecRestController.class);
	}
	
	@GetMapping
	public String getUserPecAddresses() {
		return "";
	}
}
