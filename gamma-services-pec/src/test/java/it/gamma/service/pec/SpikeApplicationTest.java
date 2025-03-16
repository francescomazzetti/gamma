package it.gamma.service.pec;

import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import it.gamma.service.pec.web.sign.PecSigner;

@SpringBootTest(
		classes = { 
				Application.class 
		},
		properties = {
		}
)

public class SpikeApplicationTest {
	@Autowired
	@Qualifier("signer.signerService") PecSigner pecSigner;
	
	@Test
	public void test1() throws NoSuchAlgorithmException, CertificateEncodingException {
		String encoded = Base64.getEncoder().encodeToString(pecSigner.cert().getEncoded());
		System.err.println(encoded);
	}
}
