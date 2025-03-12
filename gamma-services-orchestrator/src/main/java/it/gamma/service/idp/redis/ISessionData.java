package it.gamma.service.idp.redis;

public interface ISessionData
{
	String prefix();
	long expireTime();
}
