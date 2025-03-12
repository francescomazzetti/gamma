package it.gamma.service.idp.sign;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SignerService
{
	private SignerConfiguration _signerConfiguration;

	@Autowired
	public SignerService(SignerConfiguration signerConfiguration) {
		_signerConfiguration = signerConfiguration;
	}
	
	@Bean(name="signer.signerService")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public IdpSigner signer() throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, UnrecoverableEntryException {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream in = new FileInputStream(_signerConfiguration.getPath());
		keyStore.load(in, _signerConfiguration.getPassword().toCharArray());
        in.close();
        X509Certificate cert = (X509Certificate)keyStore.getCertificate(_signerConfiguration.getAlias());
        PrivateKeyEntry pk = (KeyStore.PrivateKeyEntry) keyStore.getEntry(_signerConfiguration.getAlias(), new KeyStore.PasswordProtection(_signerConfiguration.getPassword().toCharArray()));
		return new IdpSigner(cert, pk);
	}
}
