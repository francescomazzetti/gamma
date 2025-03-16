package it.gamma.service.worker.mongo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import it.gamma.service.worker.Application;
import it.gamma.service.worker.IConstants;
import it.gamma.service.worker.mongo.model.SignedAttachmentModel;

@SpringBootTest(
		classes = { 
				Application.class 
		},
		properties = {
				
		}
)
public class SignedAttachmentRepositoryTest
{
	@Autowired
	SignedAttachmentRepository signedAttachmentRepository;

	@Test
	public void test1() {
		List<SignedAttachmentModel> result = signedAttachmentRepository.findByStatus("xxx", PageRequest.of(0,3));
		assertEquals(0, result.size());
		
		result = signedAttachmentRepository.findByStatus(IConstants.STATUS_TO_BE_PROCESSED, PageRequest.of(0,3));
		assertEquals(3, result.size());
	}
}
