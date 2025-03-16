package it.gamma.service.signer.configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import it.gamma.service.signer.service.RemoteSignVerifierService;

@Configuration
@ConfigurationProperties("signer.remote.certificates")
public class RemoteCertificatesConfiguration {
	
	private String pec;

	public String getPec() {
		return pec;
	}

	public void setPec(String pec) {
		this.pec = pec;
	}
	
	@Bean(name="signer.remoteSignService")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public RemoteSignVerifierService tokenService() throws CertificateException {
		CertificateFactory fact = CertificateFactory.getInstance("X.509");
        InputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(pec));
        X509Certificate certX509 = (X509Certificate) fact.generateCertificate(is);
		return new RemoteSignVerifierService(certX509.getPublicKey());
	}
}
