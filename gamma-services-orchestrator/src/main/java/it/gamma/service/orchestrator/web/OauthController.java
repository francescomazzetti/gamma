package it.gamma.service.orchestrator.web;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.gamma.service.orchestrator.IConstants;
import it.gamma.service.orchestrator.configuration.PecServiceConfiguration;
import it.gamma.service.orchestrator.service.OauthAzRequestService;
import it.gamma.service.orchestrator.service.OauthTokenService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("oauth")
public class OauthController
{
	private Logger log;
	private OauthAzRequestService _azRequestService;
	private OauthTokenService _tokenService;
	private PecServiceConfiguration _pecServiceConfiguration;
	
	@Autowired
	public OauthController(
			@Qualifier("oauth.azRequestService") OauthAzRequestService azRequestService,
			@Qualifier("oauth.tokenRequestService") OauthTokenService tokenService,
			PecServiceConfiguration pecServiceConfiguration
			) {
		log = LoggerFactory.getLogger(OauthController.class);
		_azRequestService = azRequestService;
		_tokenService = tokenService;
		_pecServiceConfiguration = pecServiceConfiguration;
	}
	
	@GetMapping(path= "/index")
	public String index(
			HttpSession session,
			Model model)
	{
		String state = UUID.randomUUID().toString();
		session.setAttribute(IConstants.STATE, state);
		_azRequestService.requestData(model, state);
		return "index";
	}
	
	@GetMapping(path= "/client-redirect-1")
	public String oauthRedirect(
			@RequestParam(name="code", required = false) String azcode,
			@RequestParam(name="error", required = false) String error,
			@RequestParam(name="state", required = false) String state,
			HttpSession session,
			Model model)
	{
		if (azcode == null) {
			log.error("auth endpoint call ko - error: " + error);
			String newstate = UUID.randomUUID().toString();
			_azRequestService.requestData(model, newstate);
			session.setAttribute(IConstants.STATE, newstate);
			return "index";
		}
		String sessionState = (String) session.getAttribute(IConstants.STATE);
		if (!sessionState.equals(state)) {
			log.error("session value not valid");
			String newstate = UUID.randomUUID().toString();
			_azRequestService.requestData(model, newstate);
			session.setAttribute(IConstants.STATE, newstate);
			return "index";
		}
		model.addAttribute("pecUrl", _pecServiceConfiguration.getRetrieveMessagesUrl());
		model.addAttribute("pecAttachmentsUrl", _pecServiceConfiguration.getRetrieveAttachmentsUrl());
		log.info("authorization code found - state: " + state);
		_tokenService.requestData(model, azcode);
		return "index";
	}
	
	@GetMapping(path= "/client-redirect-logout")
	public String oauthRedirectLogout(
			HttpSession session,
			Model model)
	{
		session.invalidate();
		String state = UUID.randomUUID().toString();
		session.setAttribute(IConstants.STATE, state);
		_azRequestService.requestData(model, state);
		return "index";
	}
}
