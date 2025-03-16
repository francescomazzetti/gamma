package it.gamma.service.signer.service;

import java.security.PublicKey;

import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import it.gamma.service.signer.web.utils.JwsSignVerifier;

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
		String cleanToken = new JwsSignVerifier().verifySignature(userinfoToken, _idpPublicKey, log);
		if ("".equals(cleanToken)) {
			return false;
		}
		tokenBuffer.append(cleanToken);
		return true;
	}

}
