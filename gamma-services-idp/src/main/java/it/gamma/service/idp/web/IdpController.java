package it.gamma.service.idp.web;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.gamma.service.idp.IConstants;
import it.gamma.service.idp.redis.UserSessionHandler;
import it.gamma.service.idp.web.authenticator.IUserAuthenticator;
import it.gamma.service.idp.web.metadata.IMetadataReader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class IdpController
{
	private IMetadataReader _metadataReader;
	private IUserAuthenticator _userAuthenticator;
	private Logger log;
	private UserSessionHandler _userSessionHandler;

	@Autowired
	public IdpController(
			@Qualifier("metadata.readerService") IMetadataReader metadataReader,
			@Qualifier("authenticator.authenticatorService") IUserAuthenticator userAuthenticator,
			UserSessionHandler userSessionHandler
			)
	{
		_userAuthenticator = userAuthenticator;
		_metadataReader = metadataReader;
		_userSessionHandler = userSessionHandler;
		log = LoggerFactory.getLogger(IdpController.class);
	}
	
	@GetMapping("/auth")
    public String auth(
    		@RequestParam(name="client_id") String client_id,
    		@RequestParam(name="scope") String scope,
    		@RequestParam(name="redirect_uri") String redirect_uri,
    		@RequestParam(name="response_type", required = false) String response_type,
    		@RequestParam(name="state", required = false) String state,
    		Model model, HttpServletRequest request, HttpSession session) {
		JSONObject metadata = _metadataReader.read(client_id);
		if (metadata == null) {
			log.error("invalid client id received - value: " + client_id);
			return "error";
		}
		
		boolean found = false;
		JSONArray redirectUris = metadata.getJSONArray(IMetadataReader.KEY_REDIRECT_URIS);
		Iterator<Object> iterator = redirectUris.iterator();
		while (iterator.hasNext()) {
			String value = (String) iterator.next();
			if (value.equals(redirect_uri)) {
				found = true;
				break;
			}
		}
		if (!found) {
			log.error("invalid redirect uri received - issuer: " + client_id + " - redirect uri: " + redirect_uri);
			return "error";
		}
		String sessionId = UUID.randomUUID().toString();
		JSONObject authnRequest = new JSONObject();
		authnRequest.put("client_id", client_id);
		authnRequest.put("scope", scope);
		authnRequest.put("redirect_uri", redirect_uri);
		authnRequest.put("response_type", response_type);
		authnRequest.put("state", state);
		authnRequest.put("sid", sessionId);
		session.setAttribute(IConstants.KEY_SESSION_AUTHN_REQ, authnRequest.toString());
		model.addAttribute("clientName", metadata.getString(IMetadataReader.KEY_CLIENT_NAME));
		model.addAttribute("csrf", sessionId);
        return "login";
	}
	
	@PostMapping("/login")
    public String login(
    		@RequestParam(name="sesid") String csrf,
    		@RequestParam(name="username", required = false) String username,
    		@RequestParam(name="password", required = false) String password,
    		Model model, HttpServletRequest request, HttpSession session) {
		String authnRequestAsString = (String) session.getAttribute(IConstants.KEY_SESSION_AUTHN_REQ);
		if (authnRequestAsString == null) {
			log.error("no valid session found - redirecting to error page");
			return "error";
		}
		JSONObject authnRequest = new JSONObject(authnRequestAsString);
		if (!csrf.equals(authnRequest.get("sid"))) {
			log.error("no valid session found - csrf value not valid - redirecting to error page");
			return "error";
		}
		boolean authenticated = _userAuthenticator.authenticate(username, password);
		if (!authenticated) {
			log.error("authentication ko - username: " + username);
			return "login";
		}
		log.info("authentication ok - username: " + username);
		JSONObject userSession = new JSONObject();
		userSession.put("username", username);
		userSession.put(IConstants.KEY_SESSION_AUTHN_REQ, authnRequestAsString);
		String azcode = new BigInteger(130, new SecureRandom()).toString(32);
		_userSessionHandler.writeAzCode(azcode, userSession.toString());
		String redirection = authnRequest.getString("redirect_uri")+"?code="+azcode;
		log.info("authentication ok - username: " + username + " - redirection to: " + redirection);
		return "redirect:"+redirection;
	}
	
	@PostMapping("/token")
    public String token(
    		@RequestParam(name="client_id") String client_id,
    		@RequestParam(name="grant_type") String grant_type,
    		@RequestParam(name="code") String code,
    		HttpServletRequest request) {
		String authnRequestAsString = (String) session.getAttribute(IConstants.KEY_SESSION_AUTHN_REQ);
		if (authnRequestAsString == null) {
			log.error("no valid session found - redirecting to error page");
			return "error";
		}
		JSONObject authnRequest = new JSONObject(authnRequestAsString);
		if (!csrf.equals(authnRequest.get("sid"))) {
			log.error("no valid session found - csrf value not valid - redirecting to error page");
			return "error";
		}
		boolean authenticated = _userAuthenticator.authenticate(username, password);
		if (!authenticated) {
			log.error("authentication ko - username: " + username);
			return "login";
		}
		log.info("authentication ok - username: " + username);
		JSONObject userSession = new JSONObject();
		userSession.put("username", username);
		userSession.put(IConstants.KEY_SESSION_AUTHN_REQ, authnRequestAsString);
		String azcode = new BigInteger(130, new SecureRandom()).toString(32);
		_userSessionHandler.writeAzCode(azcode, userSession.toString());
		String redirection = authnRequest.getString("redirect_uri")+"?code="+azcode;
		log.info("authentication ok - username: " + username + " - redirection to: " + redirection);
		return "redirect:"+redirection;
	}
}
