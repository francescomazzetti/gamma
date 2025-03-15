package it.gamma.service.pec.web.data;

import java.io.Serializable;

public class UserPecMessage implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String id;
	private String type;
	private String address;
	private String hasAttachments;
	private String timestamp;

	public UserPecMessage(String id, String type, String address, String hasattachments, String timestamp) {
		this.setId(id);
		this.setType(type);
		this.setAddress(address);
		this.setHasAttachments(hasattachments);
		this.setTimestamp(timestamp);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHasAttachments() {
		return hasAttachments;
	}

	public void setHasAttachments(String hasAttachments) {
		this.hasAttachments = hasAttachments;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
