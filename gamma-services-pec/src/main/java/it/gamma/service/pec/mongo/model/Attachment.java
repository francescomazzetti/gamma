package it.gamma.service.pec.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Attachment
{
	@Id
	private String id;
	@Indexed(name="messageid_idx", unique = false)
	private String messageId;
	private String hashValue;
	private String name;
	private String origin;
	
	public Attachment() {
		this("", "", "", "", "");
	}
	
	public Attachment(String id, String messageId, String hashValue, String name, String origin) {
		this.id = id;
		this.messageId = messageId;
		this.hashValue = hashValue;
		this.name = name;
		this.origin = origin;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getHashValue() {
		return hashValue;
	}
	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
}
