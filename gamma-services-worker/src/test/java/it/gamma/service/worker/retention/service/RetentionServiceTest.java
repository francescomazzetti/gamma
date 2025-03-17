package it.gamma.service.worker.retention.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import it.gamma.service.worker.Application;
import it.gamma.service.worker.mongo.model.SignedAttachmentModel;
import it.gamma.service.worker.service.retention.IRetentionService;
import it.gamma.service.worker.service.retention.RetentionDataGenerator;

@SpringBootTest(
		classes = { 
			Application.class 
		},
		properties = {
			"gamma.worker.retentionStrategy=mock"
		}
)
public class RetentionServiceTest
{
	@Autowired
	@Qualifier("retention.retentionService") IRetentionService retentionService;
	
	@Test
	public void test1() {
		Logger log = LoggerFactory.getLogger(RetentionServiceTest.class);
		RetentionDataGenerator generator = new RetentionDataGenerator();
		String signatureP7m = "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";
		SignedAttachmentModel signedAttachmentModel = new SignedAttachmentModel(
				"1234", "xxxx", "local:classpath:attachments/t-0001/", 
				"Allegato1.pdf", "p7m", signatureP7m, "MZZFNC84A28H501C", "0001", new Date().getTime()+"", "");
		byte[] data = generator.generate(signedAttachmentModel , log);
		assertTrue(data.length > 0);
		
		boolean saved = retentionService.save(data, "zip", "0001", "mzzfnc84a28h501c", log);
		assertTrue(saved);
	}
}
