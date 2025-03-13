package it.gamma.service.idp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import it.gamma.service.idp.sign.IdpSigner;

@SpringBootTest(
		classes = { 
				Application.class 
		},
		properties = {
				"idp.metadata.implementation=mock",
				"spring.session.redis.namespace=spring:session:gamma-idp-test",
				"idp.authenticator.implementation=mock"
		}
)

public class SpikeApplicationTest
{
	@Autowired
	@Qualifier("signer.signerService") IdpSigner idpSigner;
	
	@Test
	public void test1() throws NoSuchAlgorithmException, CertificateEncodingException {
		String encoded = Base64.getEncoder().encodeToString(idpSigner.cert().getEncoded());
		System.err.println(encoded);
	}
}
