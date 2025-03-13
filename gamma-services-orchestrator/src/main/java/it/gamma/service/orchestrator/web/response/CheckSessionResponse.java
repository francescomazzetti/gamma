package it.gamma.service.orchestrator.web.response;

public class CheckSessionResponse
{
	private boolean valid;
	private String accessToken;
	
	public CheckSessionResponse() {
		this(false, "");
	}
	
	public CheckSessionResponse(String accessToken) {
		this(true, accessToken);
	}
	
	public CheckSessionResponse(boolean valid, String accessToken) {
		this.valid = valid;
		this.accessToken = accessToken;
	}

	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
