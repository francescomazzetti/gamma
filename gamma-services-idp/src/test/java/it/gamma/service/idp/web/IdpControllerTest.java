package it.gamma.service.idp.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;

import com.nimbusds.jose.JWSObject;

import it.gamma.service.idp.Application;
import it.gamma.service.idp.IConstants;
import it.gamma.service.idp.redis.AccessTokenSessionData;
import it.gamma.service.idp.redis.AzCodeSessionData;
import it.gamma.service.idp.redis.UserSessionHandler;
import it.gamma.service.idp.sign.IdpSigner;
import it.gamma.service.idp.web.metadata.MockMetadataReader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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


public class IdpControllerTest {
	
	private static final String KEY_SESSION_REDIS = "spring:session:gamma-idp-test";
	
	@Autowired
	private IdpController idpController;
	
	@Autowired
	private UserSessionHandler userSessionHandler;
	
	@Autowired
	private IdpSigner signer;
	
	@Test
	public void test1() throws Exception {
		String clientId = MockMetadataReader.CLIENT_ID;
		String scope = "username tenantId pec";
		String redirectUri = MockMetadataReader.REDIRECT_URI_1;
		String responseType = "code";
		String state = UUID.randomUUID().toString();
		HttpServletRequest request = new MockHttpServletRequest();
		HttpSession session = new MockHttpSession();
		Model model = new MockModel();
		String result = idpController.auth(clientId, scope, redirectUri, responseType, state, model, request, session);
		assertEquals("login", result);
		assertNotNull(session.getAttribute(IConstants.KEY_SESSION_AUTHN_REQ));
		
		String authnReq = (String) session.getAttribute(IConstants.KEY_SESSION_AUTHN_REQ);
		JSONObject authnReqJson = new JSONObject(authnReq);
		assertEquals(clientId, authnReqJson.getString("client_id"));
		
		assertNotNull(model.getAttribute("clientName"));
		assertNotNull(model.getAttribute("csrf"));
	}
	
	@Test
	public void test2() throws ParseException {
		String userSessionAT = "{\"authn-request\":\"{\\\"redirect_uri\\\":\\\"http://localhost:8082/gamma-service-orchestrator/oauth/client-redirect-1\\\",\\\"client_id\\\":\\\"https://gamma-orchestrator.com\\\",\\\"sid\\\":\\\"408b1a0a-26c9-468a-bcf4-11270e57893e\\\"}\",\"auth-complete\":\"1\",\"username\":\"francesco.mazzetti\",\"sid\":\"408b1a0a-26c9-468a-bcf4-11270e57893e\"}";
		String accToken = new BigInteger(130, new SecureRandom()).toString(32);
		userSessionHandler.write(new AccessTokenSessionData(), accToken, userSessionAT);
		HttpServletRequest request = new MockHttpServletRequest();
		ResponseEntity<String> result = idpController.userinfo("Bearer "+accToken, request);
		assertNotNull(result.getBody());
		JWSObject jwsObjectSigned = JWSObject.parse(result.getBody());
		assertEquals("francesco.mazzetti", jwsObjectSigned.getPayload().toJSONObject().getAsString("sub"));
	}
}
