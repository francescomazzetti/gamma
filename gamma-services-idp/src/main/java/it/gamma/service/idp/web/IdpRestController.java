package it.gamma.service.idp.web;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;

import it.gamma.service.idp.redis.AccessTokenSessionData;
import it.gamma.service.idp.redis.AzCodeSessionData;
import it.gamma.service.idp.redis.UserSessionHandler;
import it.gamma.service.idp.sign.IdpSigner;
import it.gamma.service.idp.web.authenticator.IUserAuthenticator;
import it.gamma.service.idp.web.factory.SignedTokenFactory;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/federation")
public class IdpRestController {
	
	private UserSessionHandler _userSessionHandler;
	private Logger log;
	private IUserAuthenticator _userAuthenticator;
	private IdpSigner _idpSigner;

	@Autowired
	public IdpRestController(
			@Qualifier("authenticator.authenticatorService") IUserAuthenticator userAuthenticator,
			@Qualifier("signer.signerService") IdpSigner idpSigner,
			UserSessionHandler userSessionHandler
			) {
		_userAuthenticator = userAuthenticator;
		_idpSigner = idpSigner;
		_userSessionHandler = userSessionHandler;
		log = LoggerFactory.getLogger(IdpRestController.class);
	}
	
	@CrossOrigin //TODO ovviamente mockato, va preso dagli origin dei sp nei metadata
	@PostMapping("/token")
    public TokenResponse token(
    		@RequestParam(name="client_id") String client_id,
    		@RequestParam(name="grant_type") String grant_type,
    		@RequestParam(name="code") String code,
    		HttpServletRequest request) {
		TokenResponse response = new TokenResponse();
		if (!"authorization_code".equals(grant_type)) {
			log.error("token endpoint ko - invalid grant type: " + grant_type);
			response.setError("invalid_request");
			return response;
		}
		String userSessionAsS = _userSessionHandler.readOnce(new AzCodeSessionData(), code);
		if (userSessionAsS == null) {
			log.error("no valid session found from azcode");
			response.setError("invalid_request");
			return response;
		}
		JSONObject userSession = new JSONObject(userSessionAsS);
		String sid = userSession.getString("sid");
		String username = userSession.getString("username");
		userSession.put("auth-complete", "1");
		String accessToken = new BigInteger(260, new SecureRandom()).toString(64); //semplificato, dovrebbe essere un jwt opaco perche' cifrato da idp
		_userSessionHandler.write(new AccessTokenSessionData(), accessToken, userSession.toString());
		try {
			String signedToken = new SignedTokenFactory().build(userSession, _userAuthenticator, _idpSigner);
			response.setIdToken(signedToken);
		} catch (JOSEException e) {
			log.error(sid + " - cannot generate id token - ex: " + e.getMessage());
		}
		response.setAccessToken(accessToken);
		response.setTokenType("Bearer");
		response.setExpiresIn(3600L);
		log.info(sid + " - token endpoint ok - username: " + username);
		return response;
	}
}
