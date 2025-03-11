package it.gamma.service.idp.web.authenticator;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="idp.authenticator")
public class AuthenticatorConfiguration
{
	private String implementation;
	
	public String getImplementation() {
		return implementation;
	}
	
	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}

}
