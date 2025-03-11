package it.gamma.service.idp.web.authenticator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AuthenticatorService
{
	private AuthenticatorConfiguration _authenticatorConfiguration;

	@Autowired
	public AuthenticatorService(AuthenticatorConfiguration authenticatorConfiguration) {
		_authenticatorConfiguration = authenticatorConfiguration;
	}
	
	@Bean(name="authenticator.authenticatorService")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public IUserAuthenticator userAuthenticator() {
		if ("mock".equals(_authenticatorConfiguration.getImplementation())) {
			return new MockUserAuthenticator();
		}
		return new LdapUserAuthenticator(_authenticatorConfiguration);
	}
}
