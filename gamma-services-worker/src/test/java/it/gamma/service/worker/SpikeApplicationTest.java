package it.gamma.service.worker;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

@SpringBootTest(
		classes = { 
				Application.class 
		},
		properties = {
				
		}
)
public class SpikeApplicationTest {
	
	@Test
	public void test1() throws FileNotFoundException {
		File file = ResourceUtils.getFile("classpath:attachments/t-0001/Allegato-1.pdf");
		assertTrue(file.exists());
	}
}
