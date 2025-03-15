package it.gamma.service.pec.service;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSAVerifier;

import net.minidev.json.JSONObject;

public class OauthUserinfoService {
	
	private String _userinfoEndpoint;
	private PublicKey _idpPublicKey;

	public OauthUserinfoService(String userinfoEndpoint, PublicKey publicKey) {
		_userinfoEndpoint = userinfoEndpoint;
		_idpPublicKey = publicKey;
	}
	
	public boolean verify(String accessToken, StringBuffer tokenBuffer, Logger log) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+accessToken);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(
				_userinfoEndpoint, 
		        HttpMethod.POST, 
		        entity, 
		        String.class);
		if (HttpStatus.OK.value() != response.getStatusCode().value()) {
			log.error(getClass().getSimpleName() + " - userinfo call ko - http status: " + response.getStatusCode().value());
			return false;
		}
		String userinfoToken = response.getBody();
		String cleanToken = verifySignature(userinfoToken, log);
		if ("".equals(cleanToken)) {
			return false;
		}
		tokenBuffer.append(cleanToken);
		return true;
	}

	private String verifySignature(String token, Logger log) {
		JWSObject jwsObjectSigned = null;
		try {
			jwsObjectSigned = JWSObject.parse(token);
		} catch (ParseException e) {
			log.error("error in userinfo token signature verify - error parsing token - " + e.getMessage());
			return "";
		}
		JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) _idpPublicKey);
		try {
			boolean signverfied = jwsObjectSigned.verify(verifier);
			if (!signverfied) {
				log.error("error in userinfo token signature verify");
				return "";
			}
			Payload payload = jwsObjectSigned.getPayload();
			JSONObject paylodToJson = payload.toJSONObject();
			return paylodToJson.toJSONString();
		} catch (JOSEException e) {
			log.error("error in userinfo token signature verify - " + e.getMessage());
			return "";
		}
	}
}
