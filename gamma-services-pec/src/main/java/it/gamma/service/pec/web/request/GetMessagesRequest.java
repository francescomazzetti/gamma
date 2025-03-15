package it.gamma.service.pec.web.request;

public class GetMessagesRequest
{
	private String type;
	private String email;
	private String hasattachments;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHasattachments() {
		return hasattachments;
	}
	public void setHasattachments(String hasattachments) {
		this.hasattachments = hasattachments;
	}
}
