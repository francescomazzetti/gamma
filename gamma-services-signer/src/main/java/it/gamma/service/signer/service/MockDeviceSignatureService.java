package it.gamma.service.signer.service;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.X509Certificate;
import java.util.Base64;

import org.springframework.util.ResourceUtils;

import it.gamma.service.signer.account.UserAccount;
import it.gamma.service.signer.web.utils.P7MCipher;

public class MockDeviceSignatureService implements ISignatureService {

	public String sign(String value, UserAccount userAccount) throws Exception {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		File file = ResourceUtils.getFile("classpath:mzzfnc84a28h501c-keystore.jks");
		FileInputStream in = new FileInputStream(file);
		keyStore.load(in, "password".toCharArray());
        in.close();
        String alias = "mzzfnc84a28h501c-sign";
		PrivateKeyEntry pk = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias , new KeyStore.PasswordProtection("password".toCharArray()));
        X509Certificate cert = (X509Certificate)keyStore.getCertificate(alias);
        byte[] signedBytes = P7MCipher.sign(value.getBytes(), pk.getPrivateKey(), cert);
        return Base64.getEncoder().encodeToString(signedBytes);
	}

}
