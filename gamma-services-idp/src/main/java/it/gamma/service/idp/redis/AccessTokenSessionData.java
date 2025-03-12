package it.gamma.service.idp.redis;

public class AccessTokenSessionData implements ISessionData {

	public String prefix() {
		return "USR_ACC_TOKEN_";
	}

	public long expireTime() {
		return 1800;
	}

}
