package it.gamma.service.orchestrator.configuration;

import java.io.Serializable;

public class TokenRequest implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String code;
	private String client_id;
	private String grant_type;
	
	public TokenRequest(String azcode, String clientId) {
		setCode(azcode);
		setClient_id(clientId);
		setGrant_type("authorization_code");
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

}
