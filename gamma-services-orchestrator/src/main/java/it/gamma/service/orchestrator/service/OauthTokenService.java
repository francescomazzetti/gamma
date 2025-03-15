package it.gamma.service.orchestrator.service;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

import org.slf4j.Logger;
import org.springframework.ui.Model;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSAVerifier;

import it.gamma.service.orchestrator.IConstants;
import net.minidev.json.JSONObject;

public class OauthTokenService {
	
	private String _tokenEndpoint;
	private String _clientId;
	private PublicKey _idpPublicKey;

	public OauthTokenService(String tokenEndpoint, String clientId, PublicKey publicKey) {
		_tokenEndpoint = tokenEndpoint;
		_clientId = clientId;
		_idpPublicKey = publicKey;
	}

	public void requestData(Model model, String azcode) {
		model.addAttribute(IConstants.STATUS, IConstants.AZCODE);
		model.addAttribute(IConstants.CLIENT_ID, _clientId);
		model.addAttribute(IConstants.GRANT_TYPE, IConstants.GRANT_TYPE_AUTHORIZATION_CODE);
		model.addAttribute(IConstants.CODE, azcode);
		model.addAttribute(IConstants.URL, _tokenEndpoint);
	}
	
	public String verify(String idToken, Logger log) throws ParseException {
		JWSObject jwsObjectSigned = JWSObject.parse(idToken);
		JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) _idpPublicKey);
		try {
			boolean signverfied = jwsObjectSigned.verify(verifier);
			if (!signverfied) {
				log.error("error in idtoken signature verify");
				return "";
			}
			Payload payload = jwsObjectSigned.getPayload();
			JSONObject paylodToJson = payload.toJSONObject();
			return paylodToJson.toJSONString();
		} catch (JOSEException e) {
			log.error("error in idtoken signature verify - " + e.getMessage());
			return "";
		}
	}
}
