package it.gamma.service.pec.mongo.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.ResourceUtils;

import it.gamma.service.pec.Application;
import it.gamma.service.pec.configuration.MongoDbPecConfiguration;

@SpringBootTest(
	classes = { 
		Application.class
	},
	properties = {
	}
)
		
public class MongoModelTest
{
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private MongoDbPecConfiguration mongoDbPecConfiguration;
	
	@Test
	public void test1() throws NoSuchAlgorithmException, IOException {
		String tenantId = "0001";
		
		List<String> userPec = new ArrayList<String>();
		userPec.add("e264f3e1-7ba9-45c9-b4db-2b8d784155a1");
		userPec.add("b425c93b-9983-4615-a18f-e1555486f508");
		userPec.add("fcddfe6d-bebb-4d70-9b1a-d11ee2641e0a");
		
		for (int i=0; i<10; i++) {
			int pecRandom = (int) ((Math.random() * (2 - 0)) + 0);
			String messageId = UUID.randomUUID().toString();
			UserMessage userMessage = new UserMessage(messageId, "francesco.mazzetti.2@pec.it", userPec.get(pecRandom), "false", "incoming", new Date().getTime()+"");
			mongoTemplate.save(userMessage, mongoDbPecConfiguration.getMessagesTablePrefix()+tenantId);
			/**
			int random = (int) ((Math.random() * (3 - 0)) + 0);
			for (int j=0; j<random; j++) {
				String filePath = "classpath:attachments/t-0001/";
				String fileName = "Allegato1.pdf";
				if (j==1) {
					fileName = "Allegato2.txt";
				}
				File file = ResourceUtils.getFile("classpath:attachments/t-0001/"+fileName);
				FileInputStream in = new FileInputStream(file);
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				byte[] hash = digest.digest(in.readAllBytes());
				String fileHash = Base64.getEncoder().encodeToString(hash);
				Attachment attachment = new Attachment(UUID.randomUUID().toString(), messageId, fileHash, fileName, "local:"+filePath);
				mongoTemplate.save(attachment, mongoDbPecConfiguration.getAttachmentsTablePrefix()+tenantId);
				in.close();
			}
			**/
		}
	}
}
