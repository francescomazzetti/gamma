package it.gamma.service.idp.web.authenticator;

import org.json.JSONObject;

public class LdapUserAuthenticator implements IUserAuthenticator {

	public LdapUserAuthenticator(AuthenticatorConfiguration _authenticatorConfiguration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean authenticate(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JSONObject getData(String username) {
		// TODO Auto-generated method stub
		return new JSONObject();
	}

}
