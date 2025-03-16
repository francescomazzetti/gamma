package it.gamma.service.signer.web.utils;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

import org.slf4j.Logger;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSAVerifier;

import net.minidev.json.JSONObject;

public class JwsSignVerifier {
	public String verifySignature(String token, PublicKey publicKey, Logger log) {
		JWSObject jwsObjectSigned = null;
		try {
			jwsObjectSigned = JWSObject.parse(token);
		} catch (ParseException e) {
			log.error("error in token signature verify - error parsing token - " + e.getMessage());
			return "";
		}
		JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicKey);
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
			log.error("error in token signature verify - " + e.getMessage());
			return "";
		}
	}
}
