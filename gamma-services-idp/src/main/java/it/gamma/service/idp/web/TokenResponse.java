package it.gamma.service.idp.web;

public class TokenResponse
{
	private String accessToken;
	private Long expiresIn;
	private String tokenType;
	private String idToken;
	private String error;
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getIdToken() {
		return idToken;
	}
	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}
}
