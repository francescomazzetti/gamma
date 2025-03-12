package it.gamma.service.idp.web.authenticator;

public interface IUserAuthenticator
{
	boolean authenticate(String username, String password);
}
