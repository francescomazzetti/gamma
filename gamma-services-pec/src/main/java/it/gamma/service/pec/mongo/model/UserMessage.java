package it.gamma.service.pec.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserMessage
{
	@Id
	private String id;
	@Indexed(name="address_idx", unique = false)
	private String address;
	@Indexed(name="pecid_idx", unique = false)
	private String pecId;
	@Indexed(name="hasattachments_idx", unique = false)
	private String hasattachments;
	@Indexed(name="type_idx", unique = false)
	private String type;
	private String timestamp;
	
	public UserMessage() {
		this("", "", "", "", "", "");
	}
	
	public UserMessage(String id, String address, String pecId, String hasAttachments, String type, String timestamp) {
		this.id = id;
		this.address = address;
		this.pecId = pecId;
		this.hasattachments = hasAttachments;
		this.type = type;
		this.setTimestamp(timestamp);
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPecId() {
		return pecId;
	}
	public void setPecId(String pecId) {
		this.pecId = pecId;
	}
	public String getHasattachments() {
		return hasattachments;
	}
	public void setHasattachments(String hasattachments) {
		this.hasattachments = hasattachments;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
