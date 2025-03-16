package it.gamma.service.signer.service;

import java.security.PublicKey;

import org.slf4j.Logger;

import it.gamma.service.signer.web.utils.JwsSignVerifier;

public class RemoteSignVerifierService
{
	private PublicKey _publicKey;

	public RemoteSignVerifierService(PublicKey publicKey) {
		_publicKey = publicKey;
	}

	public boolean verify(String jwsAsString, StringBuffer tokenBuffer, Logger log) {
		String cleanToken = new JwsSignVerifier().verifySignature(jwsAsString, _publicKey, log);
		if ("".equals(cleanToken)) {
			return false;
		}
		tokenBuffer.append(cleanToken);
		return true;
	}
}
