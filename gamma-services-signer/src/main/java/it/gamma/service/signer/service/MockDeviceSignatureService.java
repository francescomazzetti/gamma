package it.gamma.service.signer.service;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.UnrecoverableEntryException;
import java.util.Base64;

import org.springframework.util.ResourceUtils;

import it.gamma.service.signer.account.UserAccount;

public class MockDeviceSignatureService implements ISignatureService {

	public String sign(String value, UserAccount userAccount) throws Exception {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		File file = ResourceUtils.getFile("classpath:mzzfnc84a28h501c-keystore.jks");
		FileInputStream in = new FileInputStream(file);
		keyStore.load(in, "password".toCharArray());
        in.close();
        PrivateKeyEntry pk = (KeyStore.PrivateKeyEntry) keyStore.getEntry("mzzfnc84a28h501c-sign", new KeyStore.PasswordProtection("password".toCharArray()));
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initSign(pk.getPrivateKey());
        sig.update(value.getBytes());
        byte[] signatureBytes = sig.sign();
        String encoded = Base64.getEncoder().encodeToString(signatureBytes);
		return encoded;
	}

}
