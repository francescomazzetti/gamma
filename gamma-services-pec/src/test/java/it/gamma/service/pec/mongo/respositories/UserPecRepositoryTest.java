package it.gamma.service.pec.mongo.respositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import it.gamma.service.pec.Application;
import it.gamma.service.pec.mongo.model.UserPec;
import it.gamma.service.pec.mongo.repositories.UserPecRepository;

@SpringBootTest(
		classes = { 
				Application.class 
		},
		properties = {
		}
)
public class UserPecRepositoryTest {

	@Autowired
	private UserPecRepository userPecRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Test
	public void test1() {
		List<UserPec> userPecList = userPecRepository.findAll();
		//userPecRepository.save(new UserPec(UUID.randomUUID().toString(), "francesco.mazzetti-2@pec.it", "MZZFNC84A28H501C", "t-0001"));
	}
	
	@Test
	public void test2() {
		Criteria criteria = Criteria.where("address").regex("azzet");
		Query query = new Query();
		query.addCriteria(criteria);
		List<UserPec> pecs = mongoTemplate.find(query, UserPec.class);
		assertTrue(pecs.size() > 0);
		//userPecRepository.save(new UserPec(UUID.randomUUID().toString(), "francesco.mazzetti-2@pec.it", "MZZFNC84A28H501C", "t-0001"));
	}
	
	@Test
	public void test3() {
		Criteria criteria = Criteria.where("address").regex("xxx");
		Query query = new Query();
		query.addCriteria(criteria);
		List<UserPec> pecs = mongoTemplate.find(query, UserPec.class);
		assertEquals(0, pecs.size());
		//userPecRepository.save(new UserPec(UUID.randomUUID().toString(), "francesco.mazzetti-2@pec.it", "MZZFNC84A28H501C", "t-0001"));
	}
	
}
