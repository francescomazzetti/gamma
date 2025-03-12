package it.gamma.service.idp.web;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.gamma.service.idp.IConstants;
import it.gamma.service.idp.redis.AzCodeSessionData;
import it.gamma.service.idp.redis.UserSessionHandler;
import it.gamma.service.idp.web.authenticator.IUserAuthenticator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("internal")
public class InternalController
{
	private IUserAuthenticator _userAuthenticator;
	private Logger log;
	private UserSessionHandler _userSessionHandler;

	@Autowired
	public InternalController(
			@Qualifier("authenticator.authenticatorService") IUserAuthenticator userAuthenticator,
			UserSessionHandler userSessionHandler
			)
	{
		_userAuthenticator = userAuthenticator;
		_userSessionHandler = userSessionHandler;
		log = LoggerFactory.getLogger(InternalController.class);
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
		String sid = authnRequest.getString("sid");
		if (!csrf.equals(sid)) {
			log.error("no valid session found - csrf value not valid - redirecting to error page");
			return "error";
		}
		boolean authenticated = _userAuthenticator.authenticate(username, password);
		if (!authenticated) {
			log.error("authentication ko - username: " + username);
			return "login";
		}
		log.info(sid + " - authentication ok - username: " + username);
		JSONObject userSession = new JSONObject();
		userSession.put("username", username);
		userSession.put(IConstants.KEY_SESSION_AUTHN_REQ, authnRequestAsString);
		userSession.put("auth-complete", "0");
		userSession.put("sid", sid);
		String azcode = new BigInteger(130, new SecureRandom()).toString(32);
		_userSessionHandler.write(new AzCodeSessionData(), azcode, userSession.toString());
		String redirection = authnRequest.getString("redirect_uri")+"?code="+azcode;
		log.info(sid + " - authentication ok - username: " + username + " - redirection to: " + redirection);
		return "redirect:"+redirection;
	}
	
}
