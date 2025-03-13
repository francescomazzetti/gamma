package it.gamma.service.orchestrator.configuration;

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

import it.gamma.service.orchestrator.service.OauthAzRequestService;
import it.gamma.service.orchestrator.service.OauthTokenService;

@Configuration
@ConfigurationProperties(prefix="orchestrator.oauth")
public class OauthConfiguration
{
	private String clientId;
	private String redirectUri;
	private String scope;
	private String azEndpoint;
	private String tokenEndpoint;
	private String idpCertificate;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getAzEndpoint() {
		return azEndpoint;
	}
	public void setAzEndpoint(String azEndpoint) {
		this.azEndpoint = azEndpoint;
	}
	public String getTokenEndpoint() {
		return tokenEndpoint;
	}
	public void setTokenEndpoint(String tokenEndpoint) {
		this.tokenEndpoint = tokenEndpoint;
	}
	public String getIdpCertificate() {
		return idpCertificate;
	}
	public void setIdpCertificate(String idpCertificate) {
		this.idpCertificate = idpCertificate;
	}
	
	@Bean(name="oauth.azRequestService")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public OauthAzRequestService azRequestService() {
		return new OauthAzRequestService(clientId, redirectUri, scope, azEndpoint);
	}
	
	@Bean(name="oauth.tokenRequestService")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public OauthTokenService tokenService() throws CertificateException {
		CertificateFactory fact = CertificateFactory.getInstance("X.509");
        InputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(idpCertificate));
        X509Certificate certIdp509 = (X509Certificate) fact.generateCertificate(is);
		return new OauthTokenService(tokenEndpoint, clientId, certIdp509.getPublicKey());
	}
}
