package it.gamma.service.idp.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import it.gamma.service.idp.Application;
import it.gamma.service.idp.redis.AccessTokenSessionData;
import it.gamma.service.idp.redis.AzCodeSessionData;
import it.gamma.service.idp.redis.UserSessionHandler;
import it.gamma.service.idp.web.metadata.MockMetadataReader;
import jakarta.servlet.http.HttpServletRequest;

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

public class IdpRestControllerTest
{
	@Autowired
	private IdpRestController idpRestController;
	
	@Autowired
	private UserSessionHandler userSessionHandler;
	
	@Test
	public void test1() throws Exception {
		String userSession = "{\"authn-request\":\"{\\\"redirect_uri\\\":\\\"http://localhost:8082/gamma-service-orchestrator/oauth/client-redirect-1\\\",\\\"client_id\\\":\\\"https://gamma-orchestrator.com\\\",\\\"sid\\\":\\\"408b1a0a-26c9-468a-bcf4-11270e57893e\\\"}\",\"auth-complete\":\"0\",\"username\":\"francesco.mazzetti\",\"sid\":\"408b1a0a-26c9-468a-bcf4-11270e57893e\"}";
		String azcode = new BigInteger(130, new SecureRandom()).toString(32);
		userSessionHandler.write(new AzCodeSessionData(), azcode, userSession);
		String clientId = MockMetadataReader.CLIENT_ID;
		HttpServletRequest request = new MockHttpServletRequest();
		String grantType = "authorization_code";
		TokenRequest tokenRequest = new TokenRequest();
		tokenRequest.setClientId(clientId);
		tokenRequest.setCode(azcode);
		tokenRequest.setGrantType(grantType);
		TokenResponse result = idpRestController.token(clientId, grantType, azcode, request);
		assertNull(result.getError());
		assertNotNull(result.getAccessToken());
		assertEquals(3600, result.getExpiresIn());
		assertEquals("Bearer", result.getTokenType());
		
		String accessToken = result.getAccessToken();
		String accTokenSession = userSessionHandler.read(new AccessTokenSessionData(), accessToken);
		JSONObject accTokenSessionJson = new JSONObject(accTokenSession);
		assertEquals("1", accTokenSessionJson.getString("auth-complete"));
		assertEquals("francesco.mazzetti", accTokenSessionJson.getString("username"));
	}
}
