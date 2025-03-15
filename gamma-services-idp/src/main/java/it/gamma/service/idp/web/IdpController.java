package it.gamma.service.idp.web;

import java.util.Iterator;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nimbusds.jose.JOSEException;

import it.gamma.service.idp.IConstants;
import it.gamma.service.idp.redis.AccessTokenSessionData;
import it.gamma.service.idp.redis.UserSessionHandler;
import it.gamma.service.idp.sign.IdpSigner;
import it.gamma.service.idp.web.authenticator.IUserAuthenticator;
import it.gamma.service.idp.web.factory.SignedTokenFactory;
import it.gamma.service.idp.web.metadata.IMetadataReader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("federation")
public class IdpController
{
	private IUserAuthenticator _userAuthenticator;
	private IMetadataReader _metadataReader;
	private Logger log;
	private UserSessionHandler _userSessionHandler;
	private IdpSigner _idpSigner;

	@Autowired
	public IdpController(
			@Qualifier("metadata.readerService") IMetadataReader metadataReader,
			@Qualifier("authenticator.authenticatorService") IUserAuthenticator userAuthenticator,
			@Qualifier("signer.signerService") IdpSigner idpSigner,
			UserSessionHandler userSessionHandler
			)
	{
		_metadataReader = metadataReader;
		_userSessionHandler = userSessionHandler;
		_userAuthenticator = userAuthenticator;
		_idpSigner = idpSigner;
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
		log.info("auth endpoint call ok - clientid: " + client_id + " - sessionid: " + sessionId + " - redirecting to login");
        return "login";
	}
	
	@PostMapping("/userinfo")
    public ResponseEntity<String> userinfo(
    		@RequestHeader("Authorization") String authorization,
    		HttpServletRequest request) {
		if (!authorization.startsWith("Bearer ")) {
			log.error("userinfo endpoint ko - invalid authorization header");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		log.info("authorization header: " + authorization);
		int indexof = authorization.indexOf("Bearer");
		log.info("index of bearer: " + indexof);
		String accessToken = authorization.split("Bearer ")[1];
		log.info("authorization header without bearer: " + accessToken);
		String userData = _userSessionHandler.read(new AccessTokenSessionData(), accessToken);
		if (userData == null) {
			log.error("no valid session found from access token");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		JSONObject userDataJson = new JSONObject(userData);
		log.info("valid access token received - username: " + userDataJson.getString("username"));
		try {
			String signedToken = new SignedTokenFactory().build(userDataJson, _userAuthenticator, _idpSigner);
			log.info("valid access token received - username: " + userDataJson.getString("username") + " - signed token: " + signedToken);
			return ResponseEntity.ok(signedToken);
		} catch (JOSEException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
