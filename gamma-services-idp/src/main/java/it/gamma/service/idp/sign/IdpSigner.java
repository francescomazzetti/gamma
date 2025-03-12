package it.gamma.service.idp.sign;

import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.X509Certificate;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;

public class IdpSigner
{
	private X509Certificate _cert;
	private PrivateKeyEntry _pk;

	public IdpSigner(X509Certificate cert, PrivateKeyEntry pk) {
		_cert = cert; // serve solo per esporre i metadata
		_pk = pk;
	}
	
	public String sign(String value) throws JOSEException {
		JWSHeader jwsSignHeader = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID("1234567890")
                .type(JOSEObjectType.JWT).contentType("JWS").build();
		JWSSigner signer = new RSASSASigner(_pk.getPrivateKey());
		JWSObject jwsObjectSign = new JWSObject(jwsSignHeader, new Payload(value));
		jwsObjectSign.sign(signer);
		return jwsObjectSign.serialize();
	}
	
}
