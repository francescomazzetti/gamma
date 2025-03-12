package it.gamma.service.idp.web;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;

import it.gamma.service.idp.Application;
import it.gamma.service.idp.IConstants;
import it.gamma.service.idp.redis.AzCodeSessionData;
import it.gamma.service.idp.redis.UserSessionHandler;
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


public class InternalControllerTest {
	
	private static final String KEY_SESSION_REDIS = "spring:session:gamma-idp-test";
	
	@Autowired
	private InternalController internalController;
	
	@Autowired
	private UserSessionHandler userSessionHandler;
	
	@Test
	public void test1() throws Exception {
		HttpSession session = new MockHttpSession();
		String sessionId = UUID.randomUUID().toString();
		JSONObject authnRequestJson = new JSONObject();
		authnRequestJson.put("client_id", MockMetadataReader.CLIENT_ID);
		authnRequestJson.put("sid", sessionId );
		authnRequestJson.put("redirect_uri", MockMetadataReader.REDIRECT_URI_1);
		session.setAttribute(IConstants.KEY_SESSION_AUTHN_REQ, authnRequestJson.toString());
		
		HttpServletRequest request = new MockHttpServletRequest();
		Model model = new MockModel();
		String result = internalController.login(sessionId, "francesco.mazzetti", "Password1", model, request, session);
		String prefix = "redirect:"+MockMetadataReader.REDIRECT_URI_1+"?code=";
		assertTrue(result.startsWith(prefix));
		
		String azcode = result.split("code=")[1];
		String azcodeSessionData = userSessionHandler.read(new AzCodeSessionData(), azcode);
		assertNotNull(azcodeSessionData);
		System.err.println(azcodeSessionData);
	}
	
}
