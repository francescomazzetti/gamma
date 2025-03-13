package it.gamma.service.orchestrator;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.gamma.service.orchestrator.service.OauthTokenService;

@SpringBootTest(
		classes = { 
				Application.class 
		},
		properties = {
		}
)

public class SpikeApplicationTest {

	@Autowired
	private OauthTokenService oauthTokenService;
	
	@Test
	public void test1() throws ParseException {
		String idToken = "eyJraWQiOiIxMjM0NTY3ODkwIiwiY3R5IjoiSldTIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJhdWQiOiJnYW1tYS1vcmNoZXN0cmF0b3IiLCJzdWIiOiJmcmFuY2VzY28ubWF6emV0dGkiLCJpc3MiOiJnYW1tYS1pZHAiLCJjbGFpbXMiOiJ7XCJ0ZW5hbnRJZFwiOlwiMDAwMVwiLFwiY29kaWNlRmlzY2FsZVwiOlwiTVpaRk5DODRBMjhINTAxQ1wifSJ9.pz4cO-rjOx1DMn2xZlDmxfzTLmnWgHoGGmqN42JDQ6-3oZxS_OkyVlG3q4mnw9wrh3GPD8tLD-dzhM5WCVNp0JvJTHKSddvvEZ6CKFADH2kRK7X8U5O5J1xBOy5VuLIVMaaAu-CVjG1jhqpbCIvOlxYBpEAsqEI_SHC7T8WLkransSwMR7EMvS6e7MSb3eTXC-4PbSw-MHkwVYODVlWoxm47WBlzOt4IaKBWBoau_mZoajuvWTIdg_DlUi3xOywtVFF5W8kcXIXsQwMOyQVIgK-8ozwAcHBMoH7M3vpqYRskUFfp2fDbGythY-U3aq3GHHOlUlc0sQ8MQeY7z0syqQ";
		String verified = oauthTokenService.verify(idToken, LoggerFactory.getLogger(SpikeApplicationTest.class));
		System.err.println(verified);
	}
}
