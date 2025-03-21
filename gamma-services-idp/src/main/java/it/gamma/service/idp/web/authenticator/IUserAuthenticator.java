package it.gamma.service.idp.web.authenticator;

import org.json.JSONObject;

public interface IUserAuthenticator
{
	boolean authenticate(String username, String password);

	JSONObject getData(String username);
}
