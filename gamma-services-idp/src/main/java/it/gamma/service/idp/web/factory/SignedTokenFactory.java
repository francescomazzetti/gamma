package it.gamma.service.idp.web.factory;

import org.json.JSONObject;

import com.nimbusds.jose.JOSEException;

import it.gamma.service.idp.sign.IdpSigner;
import it.gamma.service.idp.web.authenticator.IUserAuthenticator;

public class SignedTokenFactory
{

	public String build(JSONObject userDataJson, IUserAuthenticator userAuthenticator, IdpSigner idpSigner) throws JOSEException {
		String authnReqAsString = userDataJson.getString("authn-request");
		JSONObject authnRequest = new JSONObject(authnReqAsString);
		JSONObject userinfoData = new JSONObject();
		userinfoData.put("aud", authnRequest.getString("client_id"));
		userinfoData.put("iss", "gamma-idp");
		userinfoData.put("sid", userDataJson.getString("sid"));
		String username = userDataJson.getString("username");
		userinfoData.put("sub", username);
		JSONObject claims = new JSONObject();
		// TODO devo farlo leggendo i valori dello scope, per adesso metto valori mock
		JSONObject userAuthData = userAuthenticator.getData(username);
		claims.put("tenantId", userAuthData.getString("tenant"));
		claims.put("codiceFiscale", userAuthData.getString("codiceFiscale"));
		userinfoData.put("claims", claims.toString());
		// TODO ecc. exp, iat, ...
		String signed = idpSigner.sign(userinfoData.toString());
		return signed;
	}

}
