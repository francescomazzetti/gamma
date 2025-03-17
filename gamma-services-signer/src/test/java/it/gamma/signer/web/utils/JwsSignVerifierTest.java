package it.gamma.signer.web.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.gamma.service.signer.Application;
import it.gamma.service.signer.configuration.OauthConfiguration;
import it.gamma.service.signer.web.utils.JwsSignVerifier;

@SpringBootTest(
		classes = { 
				Application.class 
		},
		properties = {
		}
)
public class JwsSignVerifierTest
{
	@Autowired
	private OauthConfiguration oauthConfiguration;
	
	@Test
	public void test1() throws CertificateException {
		CertificateFactory fact = CertificateFactory.getInstance("X.509");
        InputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(oauthConfiguration.getIdpCertificate()));
        X509Certificate certIdp509 = (X509Certificate) fact.generateCertificate(is);
		String token = "eyJraWQiOiIxMjM0NTY3ODkwIiwiY3R5IjoiSldTIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJhdWQiOiJodHRwczovL2dhbW1hLW9yY2hlc3RyYXRvci5jb20iLCJzdWIiOiJmcmFuY2VzY28ubWF6emV0dGkiLCJpc3MiOiJnYW1tYS1pZHAiLCJjbGFpbXMiOiJ7XCJ0ZW5hbnRJZFwiOlwiMDAwMVwiLFwiY29kaWNlRmlzY2FsZVwiOlwiTVpaRk5DODRBMjhINTAxQ1wifSIsInNpZCI6IjQwOGIxYTBhLTI2YzktNDY4YS1iY2Y0LTExMjcwZTU3ODkzZSJ9.q_fQMTrxw3O0u2iYhJkV4hNbV0tyG_o1P3hth3waZWvD_ZxQLre2mfOdAXqL_y_urEWN103G6KHTCvsGYKpRuCNqjKNveKMLpV8b6xh0jhFAjI9NkOOrEg1xYNrpiZrKQSIJpAz0ECAJbjPv21P4d1dookyKeLlJWtySZWIShjsTrhbLFbQOt4DEi4FOpX3iDERnUSvKRBWKrQqBG1tYBIFdma5S4EUUfNHKdA7IMhljWguNXVo3aTURnDpzQ_-BmgA9sHbjibBdob6Fn8M125NWxhrBOHk15iAbCLxGCUHlsop9Yve2tXtu27us4aMK9G3wgpbevjTmOCqFGlMkHQ";
		String verifiedData = new JwsSignVerifier().verifySignature(token, certIdp509.getPublicKey(), LoggerFactory.getLogger(getClass()));
		assertEquals("{\"aud\":\"https:\\/\\/gamma-orchestrator.com\",\"sub\":\"francesco.mazzetti\",\"iss\":\"gamma-idp\",\"claims\":\"{\\\"tenantId\\\":\\\"0001\\\",\\\"codiceFiscale\\\":\\\"MZZFNC84A28H501C\\\"}\",\"sid\":\"408b1a0a-26c9-468a-bcf4-11270e57893e\"}", verifiedData);
	}
}
