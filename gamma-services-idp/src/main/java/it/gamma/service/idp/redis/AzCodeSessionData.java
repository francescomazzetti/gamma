package it.gamma.service.idp.redis;

public class AzCodeSessionData implements ISessionData
{
	public String prefix() {
		return "USR_CODE_";
	}

	public long expireTime() {
		return 60;
	}

}
