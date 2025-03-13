package it.gamma.service.orchestrator.web;

import java.text.ParseException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import it.gamma.service.orchestrator.IConstants;
import it.gamma.service.orchestrator.service.OauthTokenService;
import it.gamma.service.orchestrator.web.request.InitSessionRequest;
import it.gamma.service.orchestrator.web.response.CheckSessionResponse;
import it.gamma.service.orchestrator.web.response.InitSessionResponse;
import jakarta.servlet.http.HttpSession;

@RestController
public class MainRestController
{
	private OauthTokenService _tokenService;
	private Logger log;

	public MainRestController(
			@Qualifier("oauth.tokenRequestService") OauthTokenService tokenService
			)
	{
		log = LoggerFactory.getLogger(MainRestController.class);
		_tokenService = tokenService;
	}
	
	@GetMapping(path= "/check-session", produces = "application/json")
	public CheckSessionResponse checkSession(HttpSession session, Model model)
	{
		String accessToken = (String) session.getAttribute(IConstants.ACCESS_TOKEN_SES_KEY);
		if (accessToken == null) {
			model.addAttribute("client_id", "");
			// TODO
			return new CheckSessionResponse();
		}
		return new CheckSessionResponse(accessToken);
	}
	
	@PostMapping(path= "/init-session", produces = "application/json")
	public InitSessionResponse initSession(InitSessionRequest idpTokenData, HttpSession session)
	{
		InitSessionResponse response = new InitSessionResponse();
		String accessToken = idpTokenData.getAccessToken();
		String idToken = idpTokenData.getIdToken();
		String cleanIdToken = "";
		try {
			cleanIdToken = _tokenService.verify(idToken, log);
		} catch (ParseException e) {
			log.error("error in idtoken verify - " + e.getMessage());
			response.setStatus("ko");
			return response;
		}
		if ("".equals(cleanIdToken)) {
			log.error("error in idtoken verify");
			response.setStatus("ko");
			return response;
		}
		JSONObject idTokenJson = new JSONObject(cleanIdToken);
		String username = idTokenJson.getString("sub");
		response.setStatus("ok");
		response.setUsername(username);
		response.setAccessToken(accessToken);
		session.setAttribute(IConstants.ACCESS_TOKEN, accessToken);
		log.info("init session ok - username: " + username);
		return response;
	}
}
