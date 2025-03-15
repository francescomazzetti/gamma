package it.gamma.service.pec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.ResourceUtils;

@SpringBootTest(
		classes = { 
			Application.class
		},
		properties = {
		}
	)

public class SpikeTest
{
	@Test
	public void test1() {
		String at = "Bearer xxx";
		String accessToken = at.substring(7);
		assertEquals("xxx", accessToken);
	}
	
	@Test
	public void test2() throws NoSuchAlgorithmException, IOException {
		File file = ResourceUtils.getFile("classpath:attachments/t-0001/Allegato1.pdf");
		assertTrue(file.exists());
        FileInputStream in = new FileInputStream(file);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(in.readAllBytes());
        System.err.println(Base64.getEncoder().encodeToString(hash));
	}
	
	@Test
	public void test3() {
		Criteria criteria = new Criteria();
		List<Criteria> criteriaList = new ArrayList<Criteria>();
		criteriaList.add(Criteria.where("type").is("incoming"));
		criteriaList.add(Criteria.where("hasattachments").is("true"));
		
		Pattern pattern = Pattern.compile(Pattern.quote("aaaa.i"), Pattern.CASE_INSENSITIVE);
		criteriaList.add(Criteria.where("address").regex(pattern));
		
		criteria.andOperator(criteriaList);
		Query query = new Query(criteria);
		
		System.err.println(query.toString());
	}
}
