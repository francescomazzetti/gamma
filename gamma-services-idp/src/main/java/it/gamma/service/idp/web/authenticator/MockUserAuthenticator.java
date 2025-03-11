package it.gamma.service.idp.web.authenticator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class MockUserAuthenticator implements IUserAuthenticator
{
	private static Map<String, String> _users;
	private static final String userOne = "{\"userid\":\"francesco.mazzetti\", \"password\": \"GVE/3J2k+3KkoF62aRdUjTyQ/5TVQZ4fI2PuqJ3+4d0=\", \"codiceFiscale\":\"MZZFNC84A28H501C\", \"tenant\":\"0001\"}";
	
	public MockUserAuthenticator() {
		_users = new HashMap<String, String>();
		_users.put("francesco.mazzetti", userOne);
	}
	
	public boolean authenticate(String username, String password) {
		String userData = _users.get(username);
		if (userData == null)
			return false;
		JSONObject userDataJson = new JSONObject(userData);
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			String hashValue = Base64.getEncoder().encodeToString(digest.digest(password.getBytes()));
			if (hashValue.equals(userDataJson.getString("password"))) {
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			return false;
		}
		return false;
	}
}
