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
import it.gamma.service.signer.service.OauthUserinfoService;

@Configuration
@ConfigurationProperties(prefix="pec.oauth")
public class OauthConfiguration
{
	private String userinfoEndpoint;
	private String idpCertificate;
	
	public String getIdpCertificate() {
		return idpCertificate;
	}
	public void setIdpCertificate(String idpCertificate) {
		this.idpCertificate = idpCertificate;
	}
	public String getUserinfoEndpoint() {
		return userinfoEndpoint;
	}
	public void setUserinfoEndpoint(String userinfoEndpoint) {
		this.userinfoEndpoint = userinfoEndpoint;
	}
	
	@Bean(name="oauth.userinfoService")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public OauthUserinfoService tokenService() throws CertificateException {
		CertificateFactory fact = CertificateFactory.getInstance("X.509");
        InputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(idpCertificate));
        X509Certificate certIdp509 = (X509Certificate) fact.generateCertificate(is);
		return new OauthUserinfoService(userinfoEndpoint, certIdp509.getPublicKey());
	}
}
