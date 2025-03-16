package it.gamma.service.pec.web.sign;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ResourceUtils;

@Configuration
@ConfigurationProperties(prefix="pec.sign.keystore")
public class SignerConfiguration
{
	private String path;
	private String password;
	private String alias;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	@Bean(name="signer.signerService")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public PecSigner signer() throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, UnrecoverableEntryException {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		File file = ResourceUtils.getFile(path);
		FileInputStream in = new FileInputStream(file);
		keyStore.load(in, password.toCharArray());
        in.close();
        X509Certificate cert = (X509Certificate)keyStore.getCertificate(alias);
        PrivateKeyEntry pk = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, new KeyStore.PasswordProtection(password.toCharArray()));
		return new PecSigner(cert, pk);
	}
}
