package it.gamma.service.orchestrator.service;

import org.springframework.ui.Model;
import it.gamma.service.orchestrator.IConstants;

public class OauthAzRequestService
{

	private String _clientId;
	private String _redirectUri;
	private String _scope;
	private String _oauthAzUrl;

	public OauthAzRequestService(String clientId, String redirectUri, String scope, String oauthAzUrl) {
		_clientId = clientId;
		_redirectUri = redirectUri;
		_scope = scope;
		_oauthAzUrl = oauthAzUrl;
	}

	public void requestData(Model model, String state) {
		model.addAttribute(IConstants.STATUS, IConstants.LOGGED_OUT);
		model.addAttribute(IConstants.CLIENT_ID, _clientId);
		model.addAttribute(IConstants.REDIRECT_URI, _redirectUri);
		model.addAttribute(IConstants.SCOPE, _scope);
		model.addAttribute(IConstants.STATE, state);
		model.addAttribute(IConstants.URL, _oauthAzUrl);
		
	}
}
